package com.example.Aiyl_bank_HR.service;

import com.example.Aiyl_bank_HR.dto.TransactionDto;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionService {
    @Transactional
    TransactionDto.TransferResponse transfer(TransactionDto.TransferRequest request);
}
