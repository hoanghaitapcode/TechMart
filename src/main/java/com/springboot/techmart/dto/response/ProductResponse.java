package com.springboot.techmart.dto.response;


import com.springboot.techmart.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;

    private UUID categoryId;
    private String categoryName;
    private UUID vendorId;
    private String vendorName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static ProductResponse fromEntity(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setImageUrl(product.getImageUrl());

        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getId());
            response.setCategoryName(product.getCategory().getName());
        }

        if (product.getVendor() != null) {
            response.setVendorId(product.getVendor().getId());
            response.setVendorName(product.getVendor().getUsername()); // Assuming User has getUsername()
        }

        return response;
    }

}
