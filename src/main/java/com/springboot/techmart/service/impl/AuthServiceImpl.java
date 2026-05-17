package com.springboot.techmart.service.impl;

import com.springboot.techmart.dto.response.AuthResponse;
import com.springboot.techmart.entity.Role;
import com.springboot.techmart.entity.User;
import com.springboot.techmart.entity.Wallet;
import com.springboot.techmart.exception.BadRequestException;
import com.springboot.techmart.repository.UserRepository;
import com.springboot.techmart.repository.WalletRepository;
import com.springboot.techmart.security.JwtProvider;
import com.springboot.techmart.service.AuthService;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletRepository walletRepository;
    private final JwtProvider jwtProvider;
    @Override
    public AuthResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new BadRequestException("Tên đăng nhập hoặc mật khẩu không đúng"));

        boolean isCorrectPass = passwordEncoder.matches(password, user.getPassword());
        if(!isCorrectPass) {
            throw new BadRequestException("Tên đăng nhập hoặc mật khẩu không đúng");
        }
        String token = jwtProvider.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .build();

    }

    @Override
    public AuthResponse register(String name, String email, String password) {
        Boolean isExisted = userRepository.existsByUsername(name);
        if(isExisted) {
            throw new BadRequestException("Tên đăng nhập đã tồn tại");
        }
        isExisted = userRepository.existsByEmail(email);
        if(isExisted) {
            throw new BadRequestException("Email đã tồn tại");
        }
        User user = new User();
        String encodedPassword = passwordEncoder.encode(password);
        Role role = Role.CUSTOMER;
        user.setUsername(name);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRole(role);
        userRepository.save(user);
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setUser(user);
        walletRepository.save(wallet);

        return AuthResponse.builder()
        .token(jwtProvider.generateToken(user))
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    @Override
    public void logout(HttpServletRequest request) {
        

    }
}
