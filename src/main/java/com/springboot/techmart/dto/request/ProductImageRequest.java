package com.springboot.techmart.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class ProductImageRequest {
    private MultipartFile file;
    private String altText;
    private Integer sortOrder;
    private Boolean isPrimary = false;
}