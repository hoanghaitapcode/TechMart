package com.springboot.techmart.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
@Table(name="cart_items",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"cart_id", "product_id"})})

public class CartItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @Column(nullable = false)
    @Min(1)
    private int quantity;

}
