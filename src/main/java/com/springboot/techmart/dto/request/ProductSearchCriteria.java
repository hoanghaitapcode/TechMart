package com.springboot.techmart.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
public class ProductSearchCriteria {
    private String keyWord;
    private UUID categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
