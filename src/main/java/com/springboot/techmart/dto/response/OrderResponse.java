package com.springboot.techmart.dto.response;

import com.springboot.techmart.entity.Order;
import com.springboot.techmart.entity.OrderItem;
import com.springboot.techmart.entity.Status;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private UUID id;
    private Status status;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;

    public static OrderResponse toDto(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .totalAmount(order.getTotalAmount())
                .items(order.getItems() != null ? 
                        order.getItems().stream().map(OrderItemResponse::toDto).collect(Collectors.toList()) 
                        : null)
                .build();
    }
}
