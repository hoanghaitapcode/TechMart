package com.springboot.techmart.dto.request;

import com.springboot.techmart.entity.Category;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter
public class ProductRequest {

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;
    private String description;
    @NotBlank(message="Giá tiền không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    private BigDecimal price;

    @NotBlank(message = "danh mục không được để trống")
    private UUID categoryId;
    private UUID vendorId;

    @NotNull(message="Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng tồn kho không được âm")
    private Integer stockQuantity;

    private String imageURL;
}
