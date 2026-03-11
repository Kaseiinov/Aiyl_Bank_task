package com.example.Aiyl_bank_HR.service.impl;

import com.example.Aiyl_bank_HR.Repository.AccountRepository;
import com.example.Aiyl_bank_HR.Repository.TransactionRepository;
import com.example.Aiyl_bank_HR.dto.StatementDto;
import com.example.Aiyl_bank_HR.exception.AccountNotFoundException;
import com.example.Aiyl_bank_HR.model.Account;
import com.example.Aiyl_bank_HR.model.Transaction;
import com.example.Aiyl_bank_HR.service.StatementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatementServiceImpl implements StatementService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<StatementDto.StatementEntryResponse> getStatement(
            String accountNumber,
            LocalDateTime from,
            LocalDateTime to,
            int page,
            int size) {

        log.info("Statement request: account={}, from={}, to={}, page={}, size={}",
                accountNumber, from, to, page, size);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        Pageable pageable = PageRequest.of(page, size);

        Page<Transaction> transactions = transactionRepository
                .findStatementByAccountAndDateRange(account, from, to, pageable);

        return transactions.map(tx -> new StatementDto.StatementEntryResponse(
                tx.getCreatedAt(),
                tx.getType().name(),
                tx.getAmount(),
                tx.getBalanceAfter(),
                tx.getStatus().name()
        ));
    }

}
