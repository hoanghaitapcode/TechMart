package com.springboot.techmart.dto.response;

import com.springboot.techmart.entity.Shop;
import com.springboot.techmart.entity.ShopStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ShopResponse {
    private UUID id;
    private String shopName;
    private String shopEmail;
    private String shopPhone;
    private String address;
    private ShopStatus status;
    private UUID ownerId;

    public static ShopResponse fromEntity(Shop shop) {
        return ShopResponse.builder()
                .id(shop.getId())
                .shopName(shop.getShopName())
                .shopEmail(shop.getShopEmail())
                .shopPhone(shop.getShopPhone())
                .address(shop.getAddress())
                .status(shop.getStatus())
                .ownerId(shop.getOwner().getId())
                .build();
    }
}
