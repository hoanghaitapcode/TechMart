package com.springboot.techmart.controller;

import com.springboot.techmart.dto.request.ProductRequest;
import com.springboot.techmart.dto.response.ProductResponse;
import com.springboot.techmart.entity.Product;
import com.springboot.techmart.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Product", description = "Các API quản lý Sản phẩm")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return new ResponseEntity<>(productService.CreateProduct(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.GetProductById(id));
    }
    @Operation(
            summary = "Lấy danh sách sản phẩm",
            description = "Trả về danh sách sản phẩm "
    )
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.GetAllProducts());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable UUID id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.UpdateProduct(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.DeleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
