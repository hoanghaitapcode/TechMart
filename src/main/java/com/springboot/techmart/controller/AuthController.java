package com.springboot.techmart.controller;


import com.springboot.techmart.dto.request.RegisterRequest;
import com.springboot.techmart.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Các API quản lý Đăng nhập")
@RequestMapping("/api/auth")
public class AuthController {
    private final com.springboot.techmart.service.AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody com.springboot.techmart.dto.request.LoginRequest request) {
        return ResponseEntity.ok(authService.login(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request.getUsername(), request.getEmail(), request.getPassword()));
    }
}
