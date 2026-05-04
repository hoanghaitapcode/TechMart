package com.springboot.techmart.repository;

import com.springboot.techmart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByCartIdAndProductId(UUID userId, UUID productId);
    void deleteByCartId(UUID cartId);
}
