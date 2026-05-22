package com.springboot.techmart.service;

import com.springboot.techmart.dto.request.ShopRequest;
import com.springboot.techmart.dto.response.ShopResponse;

import java.util.UUID;

public interface ShopService {
    ShopResponse upgradeToVendor(UUID userId, ShopRequest request);
    ShopResponse getMyShop(UUID userId);
}
