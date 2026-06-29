package com.eagle_bank_api.controller;

import com.eagle_bank_api.model.dto.CreateTransactionRequestDto;
import com.eagle_bank_api.model.dto.ListTransactionsResponseDto;
import com.eagle_bank_api.model.dto.TransactionResponseDto;
import com.eagle_bank_api.service.TransactionService;
import com.eagle_bank_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/accounts/{accountNumber}/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(Authentication auth,
                                                                    @PathVariable String accountNumber,
                                                                    @RequestBody @Valid CreateTransactionRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.createTransaction(userService.getAuthenticatedUser(auth), accountNumber, request));
    }

    @GetMapping("/paged")
    public ListTransactionsResponseDto listTransactionsPage(Authentication auth,
                                                        @RequestParam Integer page,
                                                        @RequestParam Integer size,
                                                        @PathVariable String accountNumber) {

        return transactionService.listTransactionsPage(userService.getAuthenticatedUser(auth), page, size, accountNumber);
    }

    @GetMapping
    public ListTransactionsResponseDto listTransactions(Authentication auth,
                                                        @PathVariable String accountNumber) {

        return transactionService.listTransactions(userService.getAuthenticatedUser(auth), accountNumber);
    }

    @GetMapping("/{transactionId}")
    public TransactionResponseDto fetchTransaction(Authentication auth,
                                                   @PathVariable String accountNumber,
                                                   @PathVariable String transactionId) {

        return transactionService.fetchTransaction(userService.getAuthenticatedUser(auth), accountNumber, transactionId);
    }
}