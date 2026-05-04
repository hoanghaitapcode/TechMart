package com.springboot.techmart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class Order extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}
