package com.springboot.techmart.controller;

import com.springboot.techmart.dto.response.OrderResponse;
import com.springboot.techmart.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;
import org.xml.sax.EntityResolver;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Các API quản lý đơn hàng")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Lấy đơn hàng của người dùng",
            description = "Trả về danh sách các đơn hàng đã được tạo bởi người dùng. API này sẽ trả về một mảng các đối tượng OrderResponse, mỗi đối tượng chứa thông tin chi tiết về một đơn hàng, bao gồm trạng thái, tổng tiền, và danh sách sản phẩm trong đơn hàng.")
    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(orderService.getOrders(userId));
    }

    @Operation(summary = "Lấy chi tiết đơn hàng",
            description = "Trả về chi tiết của một đơn hàng cụ thể theo ID đơn hàng và ID người dùng. API này sẽ trả về một đối tượng OrderResponse chứa thông tin chi tiết về đơn hàng, bao gồm trạng thái, tổng tiền, và danh sách sản phẩm trong đơn hàng. Chỉ có thể xem chi tiết đơn hàng của chính mình.")
    @GetMapping("/{userId}/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable UUID userId,@PathVariable UUID orderId){
            return ResponseEntity.ok(orderService.getOrderDetails(userId,orderId));
    }

    @PostMapping("/{userId}")
    @Operation(summary = "Thanh toán đơn hàng",
                description = "Thực hiện thanh toán cho đơn hàng của người dùng. API này sẽ kiểm tra giỏ hàng của người dùng, tính tổng tiền, kiểm tra số dư ví, và nếu đủ điều kiện, sẽ tạo một đơn hàng mới với trạng thái đã thanh toán. Sau khi thanh toán thành công, giỏ hàng sẽ được xóa và số dư ví sẽ được cập nhật. Chỉ có thể thanh toán đơn hàng của chính mình.")
    public ResponseEntity<OrderResponse> checkOut(@PathVariable UUID userId){
        return new ResponseEntity<>(orderService.checkOut(userId), HttpStatus.CREATED);
    }
    @Operation(summary = "Hủy đơn hàng",description = "Hủy một đơn hàng đã được tạo bởi người dùng. API này sẽ nhận vào ID người dùng và ID đơn hàng, sau đó thực hiện hủy đơn hàng nếu nó đang ở trạng thái có thể hủy (ví dụ: chưa được giao). Khi hủy thành công, API sẽ trả về thông tin chi tiết của đơn hàng đã bị hủy với trạng thái cập nhật. Chỉ có thể hủy đơn hàng của chính mình.")
    @PatchMapping("/{userId}/{orderId}")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable UUID userId,@PathVariable UUID orderId){
        return ResponseEntity.ok(orderService.cancelOrder(userId,orderId));
    }

}
