package com.springboot.techmart.config;

import com.springboot.techmart.security.JwtAuthenticationFilter;
import io.jsonwebtoken.security.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .cors(cors -> {}) // Kích hoạt CORS — sử dụng CorsConfigurationSource Bean từ CorsConfig
            .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF để dễ test qua Postman.
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sử dụng JWT, không cần session
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**").permitAll() //Cac endpoint liên quan đến auth (đăng nhập, đăng ký) cho phép truy cập công khai
                    .requestMatchers("/api/products/search").permitAll() // Cho phép truy cập công khai endpoint tìm kiếm sản phẩm
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated() // Các endpoint khác yêu cầu xác thực
            ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Thêm JwtAuthenticationFilter vào chuỗi filter trước UsernamePasswordAuthenticationFilter
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
