package com.springboot.techmart.specification;

import com.springboot.techmart.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductSpecification {

    public static Specification<Product> hasKeyWord(String keyWord) {
        return (root,query,cb)
                -> {
            if (keyWord == null || keyWord.trim().isEmpty())
                return null;

            return cb.like(cb.lower(root.get("name")),"%" + keyWord.trim().toLowerCase() + "%");
        };
    }
    public static Specification<Product> hasCategory(UUID categoryId){
        return(root,query,cb) ->
                categoryId == null ? null : cb.equal(root.get("category").get("id").as(String.class), categoryId);
    }
    public static Specification<Product> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice){
        return (root,query,cb) -> {
            if(minPrice == null && maxPrice == null) return null;
            if(minPrice == null) return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
            if(maxPrice == null) return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            return cb.between(root.get("price"), minPrice, maxPrice);
        };
    }
}
