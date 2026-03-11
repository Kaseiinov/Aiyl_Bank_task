package com.example.Aiyl_bank_HR.service;

import com.example.Aiyl_bank_HR.dto.StatementDto;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface StatementService {
    @Transactional(readOnly = true)
    Page<StatementDto.StatementEntryResponse> getStatement(
            String accountNumber,
            LocalDateTime from,
            LocalDateTime to,
            int page,
            int size);
}
