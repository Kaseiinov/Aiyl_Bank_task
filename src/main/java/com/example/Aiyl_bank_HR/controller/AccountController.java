package com.example.Aiyl_bank_HR.controller;

import com.example.Aiyl_bank_HR.dto.StatementDto;
import com.example.Aiyl_bank_HR.service.StatementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final StatementService statementService;

    @GetMapping("/{accountNumber}/statement")
    public ResponseEntity<Page<StatementDto.StatementEntryResponse>> getStatement(
            @PathVariable String accountNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /api/accounts/{}/statement from={} to={} page={} size={}",
                accountNumber, from, to, page, size);

        Page<StatementDto.StatementEntryResponse> statement = statementService.getStatement(
                accountNumber, from, to, page, size);

        return ResponseEntity.ok(statement);
    }

}
