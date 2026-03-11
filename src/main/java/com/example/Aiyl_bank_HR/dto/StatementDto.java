package com.example.Aiyl_bank_HR.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StatementDto {

    public record StatementEntryResponse(
            LocalDateTime operationDate,
            String operationType,
            BigDecimal amount,
            BigDecimal balanceAfter,
            String status
    ) {}

}
