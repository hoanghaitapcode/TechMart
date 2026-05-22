package com.springboot.techmart.controller;


import com.springboot.techmart.dto.request.CartItemRequest;
import com.springboot.techmart.dto.response.CartResponse;
import com.springboot.techmart.security.SecurityUtils;
import com.springboot.techmart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Các API quản lý ví")
public class CartController {

    private final CartService cartService;


    @Operation(summary = "Thêm sản phẩm vào giỏ hàng")
    @PostMapping("/me")
    public ResponseEntity<CartResponse> addCart(@Valid @RequestBody CartItemRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(cartService.addToCart(userId,request));
    }
    @Operation(summary = "Lấy giỏ hàng của người dùng")
    @GetMapping("/me")
    public ResponseEntity<CartResponse> getCart() {
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(cartService.getCart(userId));
    }

     @Operation(summary = "Cập nhật số lượng sản phẩm trong giỏ hàng")
    @PutMapping("/me/items/{productId}")
    public ResponseEntity<CartResponse> updateCart( @PathVariable UUID productId,
                                                    @RequestParam Integer quantity)
     {
         UUID userId = SecurityUtils.getCurrentUserId();
         return ResponseEntity.ok(cartService.updateCartItem(userId, productId,quantity));
     }
     @Operation(summary = "Xóa sản phẩm khỏi giỏ hàng")
    @DeleteMapping("/me/items/{productId}")
    public ResponseEntity<Void> removeCartItem(
            @PathVariable UUID productId) {
         UUID userId = SecurityUtils.getCurrentUserId();
        cartService.deleteCartItem(userId, productId);
        return ResponseEntity.noContent().build(); // Trả về 204 No Content
    }
    @Operation(summary = "Xóa toàn bộ giỏ hàng của người dùng")
    @DeleteMapping("/me")
    public ResponseEntity<Void> clearCart() {
        UUID userId = SecurityUtils.getCurrentUserId();
        cartService.clearCartItem(userId);
        return ResponseEntity.noContent().build();
    }

}
