package com.springboot.techmart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name="product_images")
@NoArgsConstructor
public class ProductImage extends BaseEntity{

    @Column(name = "url", nullable = false)
    private String imageUrl;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @JoinColumn(name="public_id", nullable = false) //Cloudniary trả về
    private String publicId;

    @Column(name="alt_text")
    private String altText;

    @Column(name="sort_order")
    private Integer sortOrder = 0;

    @Column(name="is_primary")
    private Boolean isPrimary = false;


}
