package com.springboot.techmart.dto.response;


import com.springboot.techmart.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private UUID productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private BigDecimal currentPrice;
    private Integer quantity;
    private BigDecimal subTotal;

    public static CartItemResponse toDto(CartItem item) {
        String thumbnail = null;
        if (item.getProduct().getProductImages() != null && !item.getProduct().getProductImages().isEmpty()) {
            thumbnail = item.getProduct().getProductImages().stream()
                    .filter(o -> Boolean.TRUE.equals(o.getIsPrimary()))
                    .findFirst()
                    .map(img -> img.getImageUrl())
                    .orElse(item.getProduct().getProductImages().get(0  ).getImageUrl());
        }

        return CartItemResponse.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .productImage(thumbnail)
                .price(item.getProduct().getPrice())
                .currentPrice(item.getProduct().getPrice())
                .quantity(item.getQuantity())
                .subTotal(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}
