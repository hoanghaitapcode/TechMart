package com.springboot.techmart.service;

import com.springboot.techmart.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    AuthResponse login(String username, String password);
    AuthResponse register(String username, String email, String password);
    void logout(HttpServletRequest request);
}
