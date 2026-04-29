package com.springboot.techmart.dto.response;

import com.springboot.techmart.entity.Type;
import com.springboot.techmart.entity.Transaction;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TransactionResponse {
    private UUID id;
    private BigDecimal amount;
    private Type type;
    private String description;
    private LocalDateTime createdAt;

    public static TransactionResponse toDto(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
