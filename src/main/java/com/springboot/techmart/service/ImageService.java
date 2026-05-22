package com.springboot.techmart.service;

import com.springboot.techmart.dto.response.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


public interface ImageService {

    ProductImageResponse uploadProductImage(UUID productId, MultipartFile file,
                                            String altText, Integer sortOrder, Boolean isPrimary);

    void deleteProductImage(UUID productId, UUID imageId);

    ProductImageResponse setPrimaryImage(UUID productId, UUID imageId);
}
