package com.springboot.techmart.repository;

import com.springboot.techmart.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
    List<ProductImage> findByProductIdOrderBySortOrderAsc(UUID productId);
    int countByProductId(UUID productId);
    Optional<ProductImage> findByProductIdAndIsPrimaryTrue(UUID productId);
}
