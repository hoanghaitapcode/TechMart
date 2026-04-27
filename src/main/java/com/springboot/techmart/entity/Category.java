package com.springboot.techmart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

// @SQLDelete: Ghi đè hành vi xóa — thay vì DELETE, Hibernate chạy UPDATE SET is_deleted = true
// @SQLRestriction: Thêm điều kiện ẩn vào MỌI câu SELECT — luôn lọc bỏ record đã xóa
@SQLDelete(sql = "UPDATE categories SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
@Entity
@Setter @Getter @NoArgsConstructor
@Table(name = "categories")
public class Category extends BaseEntity {
    // is_deleted đã được khai báo trong BaseEntity — KHÔNG cần lặp lại ở đây

    @Column(nullable = false, length = 100)
    private String name;

    private String description;
}
