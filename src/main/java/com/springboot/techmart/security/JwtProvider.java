package com.springboot.techmart.security;

import com.springboot.techmart.entity.User;

import java.util.UUID;

public interface JwtProvider {

    public String generateToken(User user);
    
    public boolean validateToken(String token);
    public UUID getUserIdFromToken(String token);

}
