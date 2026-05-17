package com.springboot.techmart.service;

import com.springboot.techmart.dto.request.ProductRequest;
import com.springboot.techmart.dto.request.ProductSearchCriteria;
import com.springboot.techmart.dto.response.PageResponse;
import com.springboot.techmart.dto.response.ProductResponse;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductResponse CreateProduct(ProductRequest request);
    ProductResponse GetProductById(UUID id);
    List<ProductResponse> GetAllProducts();
    ProductResponse UpdateProduct(UUID id, ProductRequest request);
    void DeleteProduct(UUID id);
    PageResponse<ProductResponse> searchProducts(ProductSearchCriteria criteria, Pageable pageable);
}
