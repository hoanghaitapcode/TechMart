package com.springboot.techmart.dto.response;


import com.springboot.techmart.entity.Wallet;
import com.springboot.techmart.repository.WalletRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@Builder
public class WalletResponse {
    private UUID id;
    private BigDecimal balance;
    private LocalDateTime updatedAt;
    private UUID userId;

    public static WalletResponse toDto(Wallet wallet) {
        return WalletResponse.builder()
                .id(wallet.getId())
                .balance(wallet.getBalance())
                .updatedAt(wallet.getUpdatedAt())
                .userId(wallet.getUser().getId())
                .build();
    }
}
