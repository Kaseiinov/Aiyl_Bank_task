package com.example.Aiyl_bank_HR.controller;

import com.example.Aiyl_bank_HR.dto.TransactionDto;
import com.example.Aiyl_bank_HR.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDto.TransferResponse> transfer(@Valid @RequestBody TransactionDto.TransferRequest request) {
        log.info("POST /api/transfers - source={}, target={}, amount={}",
                request.sourceAccountNumber(), request.targetAccountNumber(), request.amount());
        TransactionDto.TransferResponse response = transactionService.transfer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
