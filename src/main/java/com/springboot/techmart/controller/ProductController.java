package com.springboot.techmart.controller;

import com.springboot.techmart.dto.request.ProductRequest;
import com.springboot.techmart.dto.request.ProductSearchCriteria;
import com.springboot.techmart.dto.response.PageResponse;
import com.springboot.techmart.dto.response.ProductResponse;
import com.springboot.techmart.entity.Product;
import com.springboot.techmart.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Pageable;
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

    @Operation(summary = "Tạo mới sản phẩm",
    description = "Tạo mới một sản phẩm với thông tin chi tiết như tên, mô tả, giá cả, và danh mục. API này sẽ trả về sản phẩm vừa được tạo với mã ID duy nhất.")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return new ResponseEntity<>(productService.CreateProduct(request), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Lấy sản phẩm theo ID",
            description = "Trả về một sản phẩm theo ID nhằm phục vụ việc xem chi tiết sản phẩm ở page"
    )
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
    @Operation(summary = "Cập nhật sản phẩm",
               description = "Cập nhật thông tin sản phẩm theo ID. API này sẽ nhận vào một đối tượng ProductRequest chứa các trường cần cập nhật và trả về sản phẩm đã được cập nhật sau khi thực hiện thành công.")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable UUID id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.UpdateProduct(id, request));
    }

    @Operation(summary = "Xóa sản phẩm",
               description = "Xóa một sản phẩm theo ID. API này sẽ thực hiện xóa sản phẩm khỏi hệ thống và trả về mã trạng thái HTTP 204 No Content nếu xóa thành công.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.DeleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Search sản phẩm theo tiêu chí")
    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductResponse>> searchProducts(
            @ModelAttribute ProductSearchCriteria criteria, Pageable pageable) {
        return ResponseEntity.ok(productService.searchProducts(criteria, pageable));
    }
}
