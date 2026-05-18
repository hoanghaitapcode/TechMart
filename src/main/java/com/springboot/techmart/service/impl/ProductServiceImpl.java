package com.springboot.techmart.service.impl;

import com.springboot.techmart.dto.request.ProductRequest;
import com.springboot.techmart.dto.request.ProductSearchCriteria;
import com.springboot.techmart.dto.response.PageResponse;
import com.springboot.techmart.dto.response.ProductResponse;
import com.springboot.techmart.entity.Category;
import com.springboot.techmart.entity.Product;
import com.springboot.techmart.entity.User;
import com.springboot.techmart.exception.ResourceNotFoundException;
import com.springboot.techmart.repository.CategoryRepository;
import com.springboot.techmart.repository.ProductRepository;
import com.springboot.techmart.repository.UserRepository;
import com.springboot.techmart.security.SecurityUtils;
import com.springboot.techmart.service.ProductService;
import com.springboot.techmart.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public ProductResponse CreateProduct(ProductRequest request) {

        //validate Category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục"));

        // Lấy vendor từ JWT Token (tránh IDOR: không tin vào vendorId do client gửi lên)
        UUID vendorId = SecurityUtils.getCurrentUserId();
        User vendor = userRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vendor"));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);
        product.setImageUrl(request.getImageURL());
        product.setVendor(vendor); // Gọn vendor từ JWT, không từ request body

        Product savedProduct = productRepository.save(product);

        ProductResponse response = ProductResponse.fromEntity(savedProduct);
        return response;
    }

    @Override
    public ProductResponse GetProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id: " + id));
        return ProductResponse.fromEntity(product);
    }

    @Override
    public List<ProductResponse> GetAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> responses = new ArrayList<>();
        for(Product product : products) {
            responses.add(ProductResponse.fromEntity(product));
        }
        return responses;
    }

    @Override
    public ProductResponse UpdateProduct(UUID id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm để cập nhật"));

        // Nếu categoryId thay đổi, validate category mới
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục"));
            product.setCategory(category);
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageURL());
        Product updatedProduct = productRepository.save(product);

        return ProductResponse.fromEntity(updatedProduct);
    }

    @Override
    public void DeleteProduct(UUID id) {
        if(!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy sản phẩm để xóa");
        }
        productRepository.deleteById(id);
    }

    @Override
    public PageResponse<ProductResponse> searchProducts(ProductSearchCriteria criteria, Pageable pageable) {
        Specification<Product> specification = Specification.where(ProductSpecification.hasKeyWord(criteria.getKeyWord()))
                .and(ProductSpecification.hasCategory(criteria.getCategoryId()))
                .and(ProductSpecification.hasPriceBetween(criteria.getMinPrice(), criteria.getMaxPrice()));

        Page<Product> productPage = productRepository.findAll(specification, pageable);
        
        List<ProductResponse> responses = new ArrayList<>();
        for (Product product : productPage.getContent()) {
            responses.add(ProductResponse.fromEntity(product));
        }
        
        return PageResponse.<ProductResponse>builder()
                .content(responses)
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .isLast(productPage.isLast())
                .build();
    }
}
