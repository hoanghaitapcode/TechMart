package com.springboot.techmart.controller;


import com.springboot.techmart.dto.request.CartItemRequest;
import com.springboot.techmart.dto.response.CartResponse;
import com.springboot.techmart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Các API quản lý ví")
public class CartController {

    private final CartService cartService;


    @Operation(summary = "Thêm sản phẩm vào giỏ hàng")
    @PostMapping("/{userId}")
    public ResponseEntity<CartResponse> addCart(@Valid @RequestBody CartItemRequest request, @PathVariable UUID userId) {
        return ResponseEntity.ok(cartService.addToCart(userId,request));
    }
    @Operation(summary = "Lấy giỏ hàng của người dùng")
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable UUID userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

     @Operation(summary = "Cập nhật số lượng sản phẩm trong giỏ hàng")
    @PutMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartResponse> updateCart(@PathVariable UUID userId, @PathVariable UUID productId,
                                                    @RequestParam Integer quantity)
     {
         return ResponseEntity.ok(cartService.updateCartItem(userId, productId,quantity));
     }
     @Operation(summary = "Xóa sản phẩm khỏi giỏ hàng")
    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<Void> removeCartItem(
            @PathVariable UUID userId,
            @PathVariable UUID productId) {
        cartService.deleteCartItem(userId, productId);
        return ResponseEntity.noContent().build(); // Trả về 204 No Content
    }
    @Operation(summary = "Xóa toàn bộ giỏ hàng của người dùng")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable UUID userId) {
        cartService.clearCartItem(userId);
        return ResponseEntity.noContent().build();
    }

}
