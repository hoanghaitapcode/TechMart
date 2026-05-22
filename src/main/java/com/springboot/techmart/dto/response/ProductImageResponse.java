package com.springboot.techmart.dto.response;

import com.springboot.techmart.entity.ProductImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter @Builder
public class ProductImageResponse {

    private UUID id;
    private String imageUrl;
    private String altText;
    private Integer sortOrder;
    private boolean isPrimary;

    public static ProductImageResponse fromEntity(ProductImage image) {
        return ProductImageResponse.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .altText(image.getAltText())
                .sortOrder(image.getSortOrder())
                .isPrimary(image.getIsPrimary())
                .build();
    }
}
