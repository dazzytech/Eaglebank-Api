package com.eagle_bank_api.service;


import com.eagle_bank_api.mapper.TransactionMapper;
import com.eagle_bank_api.model.dto.CreateTransactionRequestDto;
import com.eagle_bank_api.model.dto.ListTransactionsResponseDto;
import com.eagle_bank_api.model.dto.TransactionResponseDto;
import com.eagle_bank_api.model.entity.BankAccount;
import com.eagle_bank_api.model.entity.Transaction;
import com.eagle_bank_api.model.entity.TransactionType;
import com.eagle_bank_api.model.entity.User;
import com.eagle_bank_api.repository.AccountRepository;
import com.eagle_bank_api.repository.TransactionRepository;
import com.eagle_bank_api.exception.ForbiddenException;
import com.eagle_bank_api.exception.InsufficientFundsException;
import com.eagle_bank_api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionResponseDto createTransaction(User user,
                                                    String accountNumber,
                                                    CreateTransactionRequestDto request) {
        BankAccount account = getAccount(user, accountNumber);

        if (!"GBP".equals(request.getCurrency())) {
            throw new IllegalArgumentException("Only GBP supported");
        }

        TransactionType type = TransactionType.valueOf(request.getType().toUpperCase());
        if (type == TransactionType.WITHDRAWAL &&
                account.getBalance() < request.getAmount()) {
            throw new InsufficientFundsException();
        }

        double newBalance = type == TransactionType.DEPOSIT
                ? account.getBalance() + request.getAmount()
                : account.getBalance() - request.getAmount();

        account.setBalance(newBalance);
        account.setUpdatedTimestamp(Instant.now());
        accountRepository.save(account);

        Transaction tx = transactionMapper.toEntity(request);
        tx.setId("tan-" + UUID.randomUUID().toString().replace("-", "").substring(0, 6));
        tx.setAccount(account);
        tx.setCreatedTimestamp(Instant.now());
        tx.setUser(user);

        transactionRepository.save(tx);
        return transactionMapper.toResponse(tx);
    }

    public ListTransactionsResponseDto listTransactionsPage(User user,
                                                        Integer pageNumber,
                                                        Integer pageSize,
                                                        String accountNumber) {
        BankAccount account = getAccount(user, accountNumber);

        Page<Transaction> txs = transactionRepository.findByAccount(account,
                PageRequest.of(pageNumber, pageSize, Sort.by("createdTimestamp").descending()));
        List<TransactionResponseDto> list = txs.stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());

        ListTransactionsResponseDto resp = new ListTransactionsResponseDto();
        resp.setTransactions(list);
        return resp;
    }

    public ListTransactionsResponseDto listTransactions(User user,
                                                        String accountNumber) {
        BankAccount account = getAccount(user, accountNumber);

        Page<Transaction> txs = transactionRepository.findByAccount(account,
                Pageable.unpaged(Sort.by("createdTimestamp").descending()));
        List<TransactionResponseDto> list = txs.stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());

        ListTransactionsResponseDto resp = new ListTransactionsResponseDto();
        resp.setTransactions(list);
        return resp;
    }

    public TransactionResponseDto fetchTransaction(User user,
                                                   String accountNumber,
                                                   String transactionId) {
        BankAccount account = getAccount(user, accountNumber);

        Transaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));

        if (!tx.getAccount().getAccountNumber().equals(account.getAccountNumber()) ||
                !account.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Transaction not associated with this user/account");
        }

        return transactionMapper.toResponse(tx);
    }

    private BankAccount getAccount(User user,
                                   String accountNumber) {
        BankAccount account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new NotFoundException("Bank account not found"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Account not associated with this user");
        }
        return account;
    }
}