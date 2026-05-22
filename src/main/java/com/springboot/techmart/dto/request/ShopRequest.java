package com.springboot.techmart.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopRequest {
    @NotBlank(message = "Tên cửa hàng không được để trống")
    private String shopName;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String shopEmail;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String shopPhone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;
}
