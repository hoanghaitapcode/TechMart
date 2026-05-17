package com.springboot.techmart.repository;

import com.springboot.techmart.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByWalletId(UUID walletId);
    List<Transaction> findByWalletIdOrderByCreatedAtDesc(UUID walletId);
}
