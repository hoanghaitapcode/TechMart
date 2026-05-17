package com.springboot.techmart.service;

import com.springboot.techmart.dto.request.CartItemRequest;
import com.springboot.techmart.dto.response.CartResponse;
import com.springboot.techmart.entity.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartService {
    CartResponse getCart(UUID userId);
    CartResponse addToCart(UUID userId, CartItemRequest item);
    CartResponse updateCartItem(UUID userId, UUID productId,Integer quantity);
    void deleteCartItem(UUID userId, UUID productId);
    void clearCartItem(UUID userId);
}
