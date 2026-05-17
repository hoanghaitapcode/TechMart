package com.springboot.techmart.controller;

import com.springboot.techmart.dto.request.WalletActionRequest;
import com.springboot.techmart.dto.response.TransactionResponse;
import com.springboot.techmart.dto.response.WalletResponse;
import com.springboot.techmart.entity.Type;
import com.springboot.techmart.exception.BadRequestException;
import com.springboot.techmart.security.SecurityUtils;
import com.springboot.techmart.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
@Tag(name = "Wallet Controller", description = "APIs quản lý ví và lịch sử giao dịch")
public class WalletController {

    private final WalletService walletService;

    @Operation(summary = "Lấy thông tin ví", description = "Trả về thông tin và số dư hiện tại của ví")
    @GetMapping("/me")
    public ResponseEntity<WalletResponse> getWallet() {
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(walletService.getWalletByUserId(userId));
    }

    @Operation(summary = "Lấy lịch sử giao dịch", description = "Trả về danh sách nạp/rút tiền sắp xếp mới nhất lên đầu")
    @GetMapping("/me/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory() {
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(walletService.getTransactionHistory(userId));
    }

    @Operation(summary = "Tạo giao dịch (Nạp/Rút)", description = "Dựa vào trường type (DEPOSIT hoặc WITHDRAW) trong body để xử lý")
    @PostMapping("/me/transactions")
    public ResponseEntity<WalletResponse> processTransaction(
            @Valid @RequestBody WalletActionRequest request) {

        // Dựa vào Noun (Transactions) và HTTP Method (POST) ta biết đây là hành động tạo giao dịch.
        // Cụ thể là giao dịch loại gì (DEPOSIT/WITHDRAW) thì nằm trong Data Payload (Body).
        // Thiết kế này loại bỏ hoàn toàn các Động từ ra khỏi URL!
        UUID userId = SecurityUtils.getCurrentUserId();
        if (request.getType() == Type.DEPOSIT) {
            return ResponseEntity.ok(walletService.deposit(userId, request));
        } else if (request.getType() == Type.WITHDRAW) {
            return ResponseEntity.ok(walletService.withdraw(userId, request));
        } else {
            throw new BadRequestException("Loại giao dịch không hợp lệ. Chỉ chấp nhận DEPOSIT hoặc WITHDRAW");
        }
    }
}
