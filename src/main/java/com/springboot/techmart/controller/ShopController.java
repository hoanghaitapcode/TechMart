package com.springboot.techmart.controller;

import com.springboot.techmart.dto.request.ShopRequest;
import com.springboot.techmart.dto.response.ShopResponse;
import com.springboot.techmart.security.SecurityUtils;
import com.springboot.techmart.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
@Tag(name = "Shop", description = "API cho phép Customer đăng ký làm người bán")
public class ShopController {

    private final ShopService shopService;

    @Operation(summary = "Đăng ký mở shop (Nâng cấp từ Customer lên Vendor)")
    @PostMapping("/register")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ShopResponse> registerShop(@Valid @RequestBody ShopRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        ShopResponse response = shopService.upgradeToVendor(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Xem thông tin shop của tôi")
    @GetMapping("/me")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<ShopResponse> getMyShop() {
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(shopService.getMyShop(userId));
    }
}
