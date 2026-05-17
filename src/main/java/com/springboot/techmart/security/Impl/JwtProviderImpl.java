package com.springboot.techmart.security.Impl;

import com.springboot.techmart.entity.User;
import com.springboot.techmart.exception.BadRequestException;
import com.springboot.techmart.security.JwtConfig;
import com.springboot.techmart.security.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtProviderImpl implements JwtProvider {

    final private JwtConfig jwtConfig;
    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(getSigningKey())
                .compact();
    }
    private SecretKey getSigningKey(){
       return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException ex) {
            // Token không đúng định dạng (bị sửa, cắt xén)
            return false;
        } catch (ExpiredJwtException ex) {
            // Token đã hết hạn
            return false;
        } catch (UnsupportedJwtException ex) {
            // Token dùng thuật toán không hỗ trợ
            return false;
        } catch (SecurityException ex) {
            // Chữ ký không hợp lệ (bị giả mạo)
            return false;
        } catch (IllegalArgumentException ex) {
            // Token rỗng hoặc null
            return false;
        }
    }

    @Override
    public UUID getUserIdFromToken(String token) {
        String subject = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        return UUID.fromString(subject);
    }
}
