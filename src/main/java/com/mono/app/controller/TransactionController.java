package com.mono.app.controller;

import com.mono.app.dto.request.TransactionRequest;
import com.mono.app.dto.response.TransactionResponse;
import com.mono.app.model.Transaction;
import com.mono.app.service.types.TransactionService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    // TODO: 2/14/2024 Добавить обработку исключений 
    
    private final TransactionService<Transaction, TransactionResponse, TransactionRequest> transactionService;

    @Autowired
    public TransactionController(
            TransactionService<Transaction, TransactionResponse, TransactionRequest> transactionService
    ) {
        this.transactionService = transactionService;
    }

    @PostMapping("/new")
    public ResponseEntity<TransactionResponse> addTransaction(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.addTransaction(request));
    }

    @GetMapping("/incomes")
    public ResponseEntity<List<TransactionResponse>> getAllWalletIncomes(
            @RequestParam(name = "walletName") String walletName
    ) {
        return ResponseEntity.ok(transactionService.getAllWalletIncomes(walletName));
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<TransactionResponse>> getAllWalletExpenses(
            @RequestParam(name = "walletName") String walletName
    ) {
        return ResponseEntity.ok(transactionService.getAllWalletExpenses(walletName));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllWalletTransactions(
            @RequestParam(name = "walletName") String walletName
    ) {
        return ResponseEntity.ok(transactionService.getAllWalletTransactions(walletName));
    }

    @GetMapping("/pages")
    public ResponseEntity<Page<TransactionResponse>> getAllWalletTransactions(
            @RequestParam(name = "walletName") String walletName,
            @RequestParam(value = "offset", defaultValue = "0", required = false) @Min(0) Integer offset,
            @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit
    ) {
        if (offset != null && limit != null && limit > 0) {
            return ResponseEntity.ok(transactionService
                    .getAllWalletTransactionsWithPagination(walletName, offset, limit));
        }
        return ResponseEntity.noContent().build();
    }
}
