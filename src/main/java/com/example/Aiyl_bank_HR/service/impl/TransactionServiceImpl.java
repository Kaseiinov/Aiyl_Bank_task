package com.example.Aiyl_bank_HR.service.impl;

import com.example.Aiyl_bank_HR.Repository.AccountRepository;
import com.example.Aiyl_bank_HR.Repository.TransactionRepository;
import com.example.Aiyl_bank_HR.dto.TransactionDto;
import com.example.Aiyl_bank_HR.dto.enums.AccountStatus;
import com.example.Aiyl_bank_HR.dto.enums.TransactionStatus;
import com.example.Aiyl_bank_HR.dto.enums.TransactionType;
import com.example.Aiyl_bank_HR.exception.AccountNotFoundException;
import com.example.Aiyl_bank_HR.exception.TransactionException;
import com.example.Aiyl_bank_HR.model.Account;
import com.example.Aiyl_bank_HR.model.Transaction;
import com.example.Aiyl_bank_HR.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    @Override
    public TransactionDto.TransferResponse transfer(TransactionDto.TransferRequest request) {
        log.info("Initiating transfer: {} -> {}, amount: {}",
                request.sourceAccountNumber(), request.targetAccountNumber(), request.amount());

        if (request.sourceAccountNumber().equals(request.targetAccountNumber())) {
            return saveFailedTransactionAndThrow(null, null, request.amount(),
                    "Source and target accounts must be different");
        }

        Account source = accountRepository.findByAccountNumber(request.sourceAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(request.sourceAccountNumber()));

        Account target = accountRepository.findByAccountNumber(request.targetAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(request.targetAccountNumber()));

        if (source.getStatus() != AccountStatus.ACTIVE) {
            return saveFailedTransactionAndThrow(source, target, request.amount(),
                    "Source account is not active: " + source.getStatus());
        }
        if (target.getStatus() != AccountStatus.ACTIVE) {
            return saveFailedTransactionAndThrow(source, target, request.amount(),
                    "Target account is not active: " + target.getStatus());
        }
        if (source.getBalance().compareTo(request.amount()) < 0) {
            return saveFailedTransactionAndThrow(source, target, request.amount(),
                    "Insufficient balance on source account");
        }

        source.setBalance(source.getBalance().subtract(request.amount()));
        target.setBalance(target.getBalance().add(request.amount()));

        accountRepository.save(source);
        accountRepository.save(target);

        Transaction txOut = Transaction.builder()
                .sourceAccount(source)
                .targetAccount(target)
                .amount(request.amount())
                .type(TransactionType.TRANSFER_OUT)
                .status(TransactionStatus.SUCCESS)
                .balanceAfter(source.getBalance())
                .build();

        Transaction txIn = Transaction.builder()
                .sourceAccount(source)
                .targetAccount(target)
                .amount(request.amount())
                .type(TransactionType.TRANSFER_IN)
                .status(TransactionStatus.SUCCESS)
                .balanceAfter(target.getBalance())
                .build();

        transactionRepository.save(txOut);
        transactionRepository.save(txIn);

        log.info("Transfer SUCCESS: txOut.id={}, txIn.id={}", txOut.getId(), txIn.getId());

        return new TransactionDto.TransferResponse(
                txOut.getId(),
                source.getAccountNumber(),
                target.getAccountNumber(),
                request.amount(),
                "SUCCESS",
                "Transfer completed successfully"
        );
    }

    private TransactionDto.TransferResponse saveFailedTransactionAndThrow(
            Account source, Account target, BigDecimal amount, String reason) {

        if (source != null && target != null) {
            Transaction failed = Transaction.builder()
                    .sourceAccount(source)
                    .targetAccount(target)
                    .amount(amount)
                    .type(TransactionType.TRANSFER_OUT)
                    .status(TransactionStatus.FAILED)
                    .balanceAfter(source.getBalance())
                    .failureReason(reason)
                    .build();
            transactionRepository.save(failed);
            log.warn("Transfer FAILED, reason: {}, saved tx id={}", reason, failed.getId());
        }

        throw new TransactionException(reason);
    }

}
