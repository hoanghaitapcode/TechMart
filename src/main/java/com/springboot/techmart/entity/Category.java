package com.springboot.techmart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter @NoArgsConstructor
@Table(name="categories")
public class Category extends BaseEntity {
    @Column(nullable = false, length = 100)
    private String name;

    private String description;
}
