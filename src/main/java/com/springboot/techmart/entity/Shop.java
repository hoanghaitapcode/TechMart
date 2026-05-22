package com.springboot.techmart.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="shops")
public class Shop extends  BaseEntity{

    @OneToOne
    @JoinColumn(name="owner_id")
    private User owner;

    private String shopName;
    private String shopEmail;
    private String shopPhone;
    private String address;

    @Enumerated(EnumType.STRING)
    private ShopStatus status;
}
