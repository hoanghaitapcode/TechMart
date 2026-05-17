package com.springboot.techmart.dto.response;

import com.springboot.techmart.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private UUID id;
    private List<CartItemResponse> items;
    private BigDecimal totalPrice;

    public static CartResponse toDto(Cart cart, List<CartItemResponse> itemResponses, BigDecimal total) {
        return CartResponse.builder()
                .id(cart.getId())
                .items(itemResponses)
                .totalPrice(total)
                .build();
    }
}
