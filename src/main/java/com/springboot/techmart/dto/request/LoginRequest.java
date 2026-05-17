package com.springboot.techmart.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@NotBlank
@Getter @Setter
public class LoginRequest {
    private String username;
    private String password;
}
