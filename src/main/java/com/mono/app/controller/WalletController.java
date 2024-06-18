package com.mono.app.controller;

import com.mono.app.dto.request.WalletRequest;
import com.mono.app.dto.response.WalletResponse;
import com.mono.app.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService service;

    @GetMapping
    public ResponseEntity<List<WalletResponse>> getAllUserWallets() {
        List<WalletResponse> response = service.getAllUserWallets();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/new")
    public ResponseEntity<Void> addWallet(@RequestBody WalletRequest request) {
        WalletResponse response = service.addWallet(request);
        
        return ResponseEntity.noContent().build();
    }
}
