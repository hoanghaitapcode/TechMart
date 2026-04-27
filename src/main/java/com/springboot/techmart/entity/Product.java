package com.springboot.techmart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

// Product CÓ @Version (Optimistic Locking) → câu SQL DELETE cần 2 tham số: id và version
// Thứ tự tham số: Hibernate truyền (id, version) theo đúng thứ tự WHERE
@SQLDelete(sql = "UPDATE products SET is_deleted = true WHERE id=? AND version=?")
@SQLRestriction("is_deleted = false")
@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor
public class Product extends BaseEntity {
    // is_deleted đã được khai báo trong BaseEntity — KHÔNG cần lặp lại ở đây

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private User vendor;

    @Column(name = "image_url")
    private String imageUrl;

    // @Version kích hoạt Optimistic Locking — bảo vệ khỏi race condition
    // Khi UPDATE/DELETE, Hibernate tự động thêm "AND version=?" vào WHERE clause
    // Nếu 2 người cùng sửa 1 sản phẩm, người đến sau sẽ bị throw OptimisticLockException
    @Version
    private Long version;
}
