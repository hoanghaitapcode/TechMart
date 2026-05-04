package com.springboot.techmart.service;

import com.springboot.techmart.dto.response.OrderResponse;

import java.util.UUID;

public interface OrderService {
    OrderResponse checkOut(UUID userId);
    OrderResponse getOrders(UUID userId);
}
