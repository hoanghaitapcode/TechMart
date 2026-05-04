package com.springboot.techmart.repository;

import com.springboot.techmart.entity.Cart;
import com.springboot.techmart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

@Repository
public interface CartRepository  extends JpaRepository<Cart, UUID> {
     Optional<Cart> findByUserId(UUID userId);
}
