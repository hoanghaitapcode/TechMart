package com.springboot.techmart.service.impl;

import com.springboot.techmart.dto.request.CategoryRequest;
import com.springboot.techmart.dto.response.CategoryResponse;
import com.springboot.techmart.entity.Category;
import com.springboot.techmart.exception.BadRequestException;
import com.springboot.techmart.exception.ResourceNotFoundException;
import com.springboot.techmart.repository.CategoryRepository;
import com.springboot.techmart.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        if(categoryRepository.existsByName(request.getName())) {
            throw new BadRequestException("Tên danh mục đã tồn tại");
        }
        
        // Chuyển DTO thành Entity
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        
        // JPA save() sẽ thực thi câu lệnh SQL INSERT
        Category savedCategory = categoryRepository.save(category);
        
        return CategoryResponse.toResponse(savedCategory);
    }

    @Override
    public CategoryResponse getCategoryById(UUID id) {
        // JPA findById() thực thi SQL SELECT
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id: " + id));
        return CategoryResponse.toResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        // JPA findAll() lấy toàn bộ dòng trong bảng
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục để cập nhật"));

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        
        // Khi entity đã có ID tồn tại, gọi save() JPA sẽ thực thi SQL UPDATE
        Category updatedCategory = categoryRepository.save(category);
        
        return CategoryResponse.toResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(UUID id) {
        if(!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy danh mục để xóa");
        }
        // JPA deleteById() thực thi SQL DELETE
        categoryRepository.deleteById(id);
    }
}
