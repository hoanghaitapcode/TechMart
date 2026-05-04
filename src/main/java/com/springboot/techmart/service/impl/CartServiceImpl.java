package com.springboot.techmart.service.impl;

import com.springboot.techmart.dto.request.CartItemRequest;
import com.springboot.techmart.dto.request.CategoryRequest;
import com.springboot.techmart.dto.response.CartItemResponse;
import com.springboot.techmart.dto.response.CartResponse;
import com.springboot.techmart.dto.response.CategoryResponse;
import com.springboot.techmart.dto.response.ProductResponse;
import com.springboot.techmart.entity.Cart;
import com.springboot.techmart.entity.CartItem;
import com.springboot.techmart.entity.Product;
import com.springboot.techmart.exception.BadRequestException;
import com.springboot.techmart.exception.ResourceNotFoundException;
import com.springboot.techmart.repository.CartItemRepository;
import com.springboot.techmart.repository.CartRepository;
import com.springboot.techmart.repository.ProductRepository;
import com.springboot.techmart.repository.UserRepository;
import com.springboot.techmart.service.CartService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private Cart getOrCreate(UUID userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(userRepository.findById(userId).orElseThrow(()
                            -> new ResourceNotFoundException("Không tìm thấy người dùng với id: " + userId)));
                    return cartRepository.save(newCart);
                });

        return cart;
    }
    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(UUID userId) {
        Cart cart = getOrCreate(userId);

        List<CartItemResponse> items = cart.getItems().stream()
                .map(item -> CartItemResponse.toDto(item))
                .toList();

        BigDecimal totalPrice = items.stream()
                .map(item ->item.getSubTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        CartResponse response = CartResponse.toDto(cart, items  , totalPrice);
        return response;
    }


    @Override
    @Transactional
    public CartResponse addToCart(UUID userId, CartItemRequest itemRequest) {
        Cart cart = getOrCreate(userId);

        List<CartItem> items = cart.getItems();
        Optional<CartItem> existedProduct = items.stream()
                .filter(item -> item.getProduct().getId().equals(itemRequest.getProductId()))
                .findFirst();
        //Vì chỉ tồn tại 1 cặp cart_id và product_id (unique constraints ở entity)
        if(existedProduct.isPresent()) {
            CartItem item = existedProduct.get();
            int newQuantity = item.getQuantity() + itemRequest.getQuantity();
            return updateCartItem(userId, itemRequest.getProductId(), newQuantity);
            }

        CartItem addedItem = new CartItem();

        Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id: " + itemRequest.getProductId()));
        if(itemRequest.getQuantity()>product.getStockQuantity()) {
            throw new BadRequestException("Số lượng sản phẩm không đủ trong kho");
        }
        addedItem.setCart(cart);
        addedItem.setProduct(product);
        addedItem.setQuantity(itemRequest.getQuantity());


        items.add(addedItem);
        cartRepository.save(cart);

        return getCart(userId);


    }

    @Override
    @Transactional
    public CartResponse updateCartItem(UUID userId, UUID productId, Integer quantity) {
        Cart cart = getOrCreate(userId);
        List<CartItem> items = cart.getItems();
        CartItem itemUpdated = items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                        .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng với id: " + productId));
        if(quantity>itemUpdated.getProduct().getStockQuantity()) {
            throw new BadRequestException("Số lượng sản phẩm trong kho không đủ");
        }
        itemUpdated.setQuantity(quantity);
        cartRepository.save(cart);

         return getCart(userId);
    }

    @Override
    @Transactional
    public void deleteCartItem(UUID userId, UUID productId) {
        Cart cart = getOrCreate(userId);
        List<CartItem> items = cart.getItems();
        CartItem itemDeleted = items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng với id: " + productId));
        items.remove(itemDeleted);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCartItem(UUID userId) {
        Cart cart = getOrCreate(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
