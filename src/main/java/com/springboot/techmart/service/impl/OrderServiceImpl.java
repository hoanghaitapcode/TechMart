package com.springboot.techmart.service.impl;

import com.springboot.techmart.dto.response.OrderResponse;
import com.springboot.techmart.entity.*;
import com.springboot.techmart.exception.BadRequestException;
import com.springboot.techmart.repository.*;
import com.springboot.techmart.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    final OrderRepository orderRepository;
    final OrderItemRepository orderItemRepository;
    final CartRepository cartRepository;
    final ProductRepository productRepository;
    final WalletRepository walletRepository;
    final TransactionRepository transactionRepository;
    @Override
    @Transactional
    public OrderResponse checkOut(UUID userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(()->new BadRequestException("Giỏ hàng không tồn tại"));
        if(cart.getItems().isEmpty()) {
            throw new BadRequestException("Giỏ hàng trống");
        }
        //Tính tổng tiên của giỏ hàng và kiểm tra tồn kho từng món
        BigDecimal totalAmount = BigDecimal.ZERO;
        for(CartItem item:cart.getItems()) {
            Product product = item.getProduct();
            if(product.getStockQuantity() < item.getQuantity()) {
                throw new BadRequestException("Sản phẩm " + product.getName() + " không đủ hàng tồn kho để thực hiện giao dịch");
            }
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(()->new BadRequestException("Ví không tồn tại"));


        //So sánh số dư ví nếu fail
        if(wallet.getBalance().compareTo(totalAmount) < 0) {
            throw new BadRequestException("Số dư ví không đủ để thực hiện giao dịch");
        }
        //Nếu đủ số dư
        wallet.setBalance(wallet.getBalance().subtract(totalAmount));
        walletRepository.save(wallet);

        //Tạo Order và trừ tồn kho
        Order order = new Order();
        order.setCustomer(wallet.getUser());
        order.setTotalAmount(totalAmount);
        order.setStatus(Status.PAID);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(item.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .build();
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);
        orderRepository.save(order); // CascadeType.ALL → Tự lưu OrderItems

        // 6. Ghi Transaction log
        Transaction tx = Transaction.builder()
                .wallet(wallet)
                .amount(totalAmount)
                .type(Type.PAYMENT)
                .description("Thanh toán đơn hàng #" + order.getId())
                .build();
        transactionRepository.save(tx);

        // 7. Xóa giỏ hàng
        cart.getItems().clear(); // orphanRemoval = true → DB tự xóa
        cartRepository.save(cart);

        return OrderResponse.toDto(order);

    }

    @Override
    public List<OrderResponse> getOrders(UUID userId) {
        List<Order> order = orderRepository.findByCustomerIdOrderByCreatedAtDesc(userId);

        List<OrderResponse> responses = order.stream()
                .map(OrderResponse::toDto)
                .toList();

        return responses;
    }

    @Override
    public OrderResponse getOrderDetails(UUID userId, UUID orderId) {
        List<Order> order = orderRepository.findByCustomerIdOrderByCreatedAtDesc(userId);
        Order orderDetail = order.stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElse(null);

        return OrderResponse.toDto(orderDetail);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(UUID userId, UUID orderId) {
        Order order = orderRepository.findByCustomerIdOrderByCreatedAtDesc(userId).stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đơn hàng với ID: " + orderId));

        if(!order.getStatus().isCancellable()) {
            throw new BadRequestException("Đơn hàng không thể hủy");
        }
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("Ví không tồn tại"));
        wallet.setBalance(wallet.getBalance().add(order.getTotalAmount()));
        walletRepository.save(wallet);
        for(OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }
        Transaction tx = Transaction.builder()
                .wallet(wallet)
                .amount(order.getTotalAmount())
                .type(Type.REFUND)
                .description("Hoàn tiền cho đơn hàng #" + order.getId())
                .build();
        transactionRepository.save(tx);
        order.setStatus(Status.CANCELLED);
        orderRepository.save(order);
        return OrderResponse.toDto(order);
    }

}
