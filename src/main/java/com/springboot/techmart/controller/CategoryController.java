package com.springboot.techmart.controller;

import com.springboot.techmart.dto.request.CategoryRequest;
import com.springboot.techmart.dto.response.CategoryResponse;
import com.springboot.techmart.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Các API quản lý danh mục sản phẩm")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tạo mới danh mục",
            description = "Tạo mới một danh mục với thông tin chi tiết như tên và mô tả. API này sẽ trả về danh mục vừa được tạo với mã ID duy nhất. Chỉ Admin")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        return new ResponseEntity<>(categoryService.createCategory(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Lấy tất cả danh mục",
            description = "Trả về danh sách tất cả các danh mục hiện có trong hệ thống. API này sẽ trả về một mảng các đối tượng CategoryResponse, mỗi đối tượng chứa thông tin chi tiết về một danh mục.")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Lấy danh mục theo ID",
            description = "Trả về một danh mục theo ID nhằm phục vụ việc xem chi tiết danh mục ở page")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cập nhật danh mục",
            description = "Cập nhật thông tin danh mục theo ID. API này sẽ nhận vào một đối tượng CategoryRequest chứa các trường cần cập nhật và trả về danh mục đã được cập nhật sau khi thực hiện thành công. Chỉ Admin")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable UUID id, @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xóa danh mục",
            description = "Xóa một danh mục theo ID. API này sẽ thực hiện xóa danh mục khỏi hệ thống và trả về mã trạng thái HTTP 204 No Content nếu xóa thành công. Chỉ Admin soft delete")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
