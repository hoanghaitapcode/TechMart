package com.springboot.techmart.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Type type; // DEPOSIT, WITHDRAW, PAYMENT, REFUND

    private String description;
}
