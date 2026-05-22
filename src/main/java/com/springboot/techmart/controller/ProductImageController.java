package com.springboot.techmart.controller;


import com.springboot.techmart.dto.request.ProductImageRequest;
import com.springboot.techmart.dto.response.ProductImageResponse;
import com.springboot.techmart.entity.ProductImage;
import com.springboot.techmart.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/product/{productId}/images")
@RequiredArgsConstructor
@Tag(name = "Product Image", description = "APIs quản lý ảnh sản phẩm")
@PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
public class ProductImageController {
    private final ImageService imageService;

    @Operation(summary = "Upload ảnh cho sản phẩm",
            description = "Upload 1 ảnh lên Cloudinary và gắn vào sản phẩm. Tối đa 8 ảnh/sản phẩm, file ≤ 5MB, chỉ JPG/PNG/WEBP.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductImageResponse> uploadProductImage(@PathVariable UUID productId, @ModelAttribute ProductImageRequest request) {
        return new ResponseEntity<>(imageService.uploadProductImage(productId, request.getFile(), request.getAltText(), request.getSortOrder(), request.getIsPrimary()), HttpStatus.CREATED);
    }

    @Operation(summary = "Xóa ảnh sản phẩm",
            description = "Xóa ảnh khỏi Cloudinary và Database")
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable UUID productId, @PathVariable UUID imageId) {
        imageService.deleteProductImage(productId, imageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{imageId}")
    public ResponseEntity<ProductImageResponse> setPrimary(@PathVariable UUID productId,@PathVariable UUID imageId) {
        imageService.setPrimaryImage(productId, imageId);
        return ResponseEntity.ok().build();
    }
}
