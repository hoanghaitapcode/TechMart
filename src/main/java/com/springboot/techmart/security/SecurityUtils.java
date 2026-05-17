package com.springboot.techmart.security;

import com.springboot.techmart.exception.UnAuthorizedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.UUID;
import org.springframework.security.core.Authentication;

public class SecurityUtils {
    public static UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnAuthorizedException("Chưa đăng nhập");
        }
        
        Object principal = authentication.getPrincipal();
        
        // Thông thường Spring Security lưu principal dưới dạng UserDetails
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            String userIdString = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
            return UUID.fromString(userIdString);
        }
        
        // Đề phòng trường hợp principal được lưu trực tiếp dạng String
        if (principal instanceof String) {
            return UUID.fromString((String) principal);
        }
        
        throw new UnAuthorizedException("Không thể lấy thông tin người dùng");
    }
}
