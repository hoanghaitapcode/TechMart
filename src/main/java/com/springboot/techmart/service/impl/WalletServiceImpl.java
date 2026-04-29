package com.springboot.techmart.service.impl;

import com.springboot.techmart.dto.request.WalletActionRequest;
import com.springboot.techmart.dto.response.TransactionResponse;
import com.springboot.techmart.dto.response.WalletResponse;
import com.springboot.techmart.entity.Transaction;
import com.springboot.techmart.entity.Type;
import com.springboot.techmart.entity.Wallet;
import com.springboot.techmart.exception.BadRequestException;
import com.springboot.techmart.exception.ResourceNotFoundException;
import com.springboot.techmart.repository.TransactionRepository;
import com.springboot.techmart.repository.WalletRepository;
import com.springboot.techmart.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;


    @Override
    public WalletResponse getWalletByUserId(UUID userId) {
        Wallet wallet  = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ví cho User có ID: " + userId));

        return WalletResponse.toDto(wallet);
    }

    @Override
    public BigDecimal getWalletBalanceByUserId(UUID userId) {
        Wallet wallet  = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ví cho User có ID: " + userId));
        return wallet.getBalance();
    }

    @Override
    @Transactional
    public WalletResponse deposit(UUID userId, WalletActionRequest request) {
        Wallet wallet  = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ví cho User có ID: " + userId));
        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        try { Thread.sleep(5000); } catch (InterruptedException e) {}
        walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .wallet(wallet)
                .amount(request.getAmount())
                .type(Type.DEPOSIT)
                .description("Nạp tiền vào ví")
                .build();
        return WalletResponse.toDto(wallet);
    }

    @Override
    @Transactional
    public WalletResponse withdraw(UUID userId, WalletActionRequest request) {
        Wallet wallet  = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ví cho User có ID: " + userId));
        if(wallet.getBalance().compareTo(request.getAmount()) <0 ) {
            throw new BadRequestException("Số dư không đủ để rút");
        }
        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .wallet(wallet)
                .amount(request.getAmount())
                .type(Type.WITHDRAW)
                .description("Rút tiền khỏi ví")
                .build();
        return WalletResponse.toDto(wallet);

    }

    @Override
    public List<TransactionResponse> getTransactionHistory(UUID userId) {
        Wallet wallet  = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ví cho User có ID: " + userId));

        List<Transaction> transactions = transactionRepository.findByWalletIdOrderByCreatedAtDesc(wallet.getId());
        return transactions.stream()
                .map(TransactionResponse::toDto)
                .toList();
    }
}
