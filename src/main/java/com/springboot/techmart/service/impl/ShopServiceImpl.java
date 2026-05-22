package com.springboot.techmart.service.impl;

import com.springboot.techmart.dto.request.ShopRequest;
import com.springboot.techmart.dto.response.ShopResponse;
import com.springboot.techmart.entity.Role;
import com.springboot.techmart.entity.Shop;
import com.springboot.techmart.entity.ShopStatus;
import com.springboot.techmart.entity.User;
import com.springboot.techmart.exception.BadRequestException;
import com.springboot.techmart.exception.ResourceNotFoundException;
import com.springboot.techmart.repository.ShopRepository;
import com.springboot.techmart.repository.UserRepository;
import com.springboot.techmart.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ShopResponse upgradeToVendor(UUID userId, ShopRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        if (user.getRole() == Role.VENDOR || user.getRole() == Role.ADMIN) {
            throw new BadRequestException("Tài khoản của bạn đã là người bán hoặc admin");
        }

        if (shopRepository.existsByOwnerId(userId)) {
            throw new BadRequestException("Bạn đã gửi yêu cầu đăng ký shop rồi");
        }

        if (shopRepository.existsByShopName(request.getShopName())) {
            throw new BadRequestException("Tên shop đã tồn tại, vui lòng chọn tên khác");
        }

        Shop shop = new Shop();
        shop.setOwner(user);
        shop.setShopName(request.getShopName());
        shop.setShopEmail(request.getShopEmail());
        shop.setShopPhone(request.getShopPhone());
        shop.setAddress(request.getAddress());
        
        // Tạm thời set APPROVED luôn để làm các phase sau cho dễ.
        // Thực tế có thể set PENDING và chờ Admin duyệt.
        shop.setStatus(ShopStatus.APPROVED);
        
        shopRepository.save(shop);

        // Nâng cấp quyền ngay lập tức
        user.setRole(Role.VENDOR);
        userRepository.save(user);

        return ShopResponse.fromEntity(shop);
    }

    @Override
    public ShopResponse getMyShop(UUID userId) {
        Shop shop = shopRepository.findByOwnerId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Bạn chưa có shop nào"));
        return ShopResponse.fromEntity(shop);
    }
}
