package com.springboot.techmart.service;

import com.springboot.techmart.dto.response.OrderResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    OrderResponse checkOut(UUID userId);
    List<OrderResponse> getOrders(UUID userId);
    OrderResponse getOrderDetails(UUID userId,UUID orderId);
    OrderResponse cancelOrder(UUID userId, UUID orderId);

}
