package com.springboot.techmart.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Shop extends  BaseEntity{

    @OneToOne(mappedBy = "shop")
    private User owner;
    @NotBlank(message = "Tên cửa hàng không được để trống")
    private String shopName;
    @Email
    private String shopEmail;
    private String shopPhone;
    private String address;

}
