package com.springboot.techmart.dto.response;

import com.springboot.techmart.entity.Role;
import lombok.*;

@Getter @Setter @AllArgsConstructor
@Builder
public class AuthResponse {
    String token;
    String username;
    Role role;
}
