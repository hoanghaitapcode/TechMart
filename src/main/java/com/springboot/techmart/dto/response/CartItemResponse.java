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
        return CartItemResponse.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .productImage(item.getProduct().getImageUrl())
                .price(item.getProduct().getPrice())
                .currentPrice(item.getProduct().getPrice())
                .quantity(item.getQuantity())
                .subTotal(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}
