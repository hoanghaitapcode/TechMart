package com.springboot.techmart.repository;

import com.springboot.techmart.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    // Spring Data JPA sẽ tự động tạo ra các hàm như save(), findById(), findAll(), deleteById()
    
    // Bạn có thể định nghĩa thêm các hàm tìm kiếm theo tên thuộc tính (Query Methods)
    // Ví dụ: Tìm danh mục theo tên, trả về true/false nếu tồn tại
    boolean existsByName(String name);
}
