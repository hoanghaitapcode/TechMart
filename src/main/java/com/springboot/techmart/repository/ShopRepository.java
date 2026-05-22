package com.springboot.techmart.repository;

import com.springboot.techmart.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopRepository extends JpaRepository<Shop, UUID> {
    boolean existsByOwnerId(UUID ownerId);
    boolean existsByShopName(String shopName);
    Optional<Shop> findByOwnerId(UUID ownerId);
}
