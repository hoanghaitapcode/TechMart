package com.springboot.techmart.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Type type; // DEPOSIT, WITHDRAW, PAYMENT, REFUND

    private String description;
}
