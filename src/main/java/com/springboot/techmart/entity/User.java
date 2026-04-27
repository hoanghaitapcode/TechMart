package com.springboot.techmart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

// Khi Admin "vô hiệu hóa" tài khoản User, ta không xóa thật
// Tài khoản bị đánh dấu is_deleted = true → Không đăng nhập được, không hiện trong danh sách
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
@Getter @Setter
@Entity @NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    // is_deleted đã được khai báo trong BaseEntity — KHÔNG cần lặp lại ở đây

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;
}
