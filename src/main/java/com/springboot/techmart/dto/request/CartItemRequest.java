package com.springboot.techmart.dto.request;

import com.springboot.techmart.dto.response.CartItemResponse;
import com.springboot.techmart.entity.CartItem;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CartItemRequest {

    private UUID productId;
    @Min(1)
    private int quantity;

}
