package com.springboot.techmart.dto.response;


import com.springboot.techmart.entity.Product;
import com.springboot.techmart.entity.ProductImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private List<ProductImageResponse> images;
    private String thumbnailUrl;

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
        List<ProductImageResponse> imageResponses = new ArrayList<>();
        String thumbnail = null;

        if(product.getProductImages()!=null && !product.getProductImages().isEmpty()){
            for (ProductImage image : product.getProductImages()) {
                imageResponses.add(ProductImageResponse.fromEntity(image));
                if (image.getIsPrimary() != null && image.getIsPrimary()) {
                    thumbnail = image.getImageUrl();
                }
            }
        }
        if(thumbnail==null) {
            thumbnail = product.getProductImages().getFirst().getImageUrl();
        }

        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getId());
            response.setCategoryName(product.getCategory().getName());
        }

        if (product.getVendor() != null) {
            response.setVendorId(product.getVendor().getId());
            response.setVendorName(product.getVendor().getShopName()); // Assuming User has getUsername()
        }

        return response;
    }

}
