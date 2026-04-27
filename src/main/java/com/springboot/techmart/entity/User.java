package com.springboot.techmart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity @NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    @Column(unique = true,nullable = false)
    private  String email;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;


}
