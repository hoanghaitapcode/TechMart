package com.springboot.techmart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "wallets")
public class Wallet extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal balance = BigDecimal.ZERO;

    @Version // Quan trọng cho tính toàn vẹn tài chính
    private Long version;
}
