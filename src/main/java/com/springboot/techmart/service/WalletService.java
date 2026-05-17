package com.springboot.techmart.service;


import com.springboot.techmart.dto.request.WalletActionRequest;
import com.springboot.techmart.dto.response.TransactionResponse;
import com.springboot.techmart.dto.response.WalletResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public interface WalletService {
    WalletResponse getWalletByUserId(UUID userId);
    BigDecimal getWalletBalanceByUserId(UUID userId);
    WalletResponse deposit(UUID userId, WalletActionRequest request);
    WalletResponse withdraw(UUID userId, WalletActionRequest request);
    List<TransactionResponse> getTransactionHistory(UUID userId);
}
