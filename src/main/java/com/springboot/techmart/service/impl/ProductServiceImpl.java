package com.springboot.techmart.service.impl;

import com.springboot.techmart.dto.request.ProductRequest;
import com.springboot.techmart.dto.response.ProductResponse;
import com.springboot.techmart.entity.Category;
import com.springboot.techmart.entity.Product;
import com.springboot.techmart.repository.CategoryRepository;
import com.springboot.techmart.repository.ProductRepository;
import com.springboot.techmart.service.CategoryService;
import com.springboot.techmart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    //private final ProductService productService;

    @Override
    public ProductResponse CreateProduct(ProductRequest request) {

        //validate Category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);
        product.setImageUrl(request.getImageURL());

        Product savedProduct = productRepository.save(product);

        ProductResponse response = ProductResponse.fromEntity(savedProduct);
        return response;
    }
    @Override
    public ProductResponse GetProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
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
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm để cập nhật"));
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
            throw new RuntimeException("Không tìm thấy sản phẩm để xóa");
        }
        productRepository.deleteById(id);
    }
}
