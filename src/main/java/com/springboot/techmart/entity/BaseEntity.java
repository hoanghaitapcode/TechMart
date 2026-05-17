package com.springboot.techmart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter @Setter @NoArgsConstructor
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ═══════════════════════════════════════════════
    // SOFT DELETE PATTERN — Dùng chung cho mọi Entity
    // ═══════════════════════════════════════════════
    // Mặc định = false (chưa bị xóa)
    // Khi "xóa", chỉ SET cột này thành true, không DELETE khỏi DB
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;
}
