package com.eagle_bank_api.controller;

import com.eagle_bank_api.model.dto.BankAccountResponseDto;
import com.eagle_bank_api.model.dto.CreateBankAccountRequestDto;
import com.eagle_bank_api.model.dto.ListBankAccountsResponseDto;
import com.eagle_bank_api.model.dto.UpdateBankAccountRequestDto;
import com.eagle_bank_api.service.AccountService;
import com.eagle_bank_api.service.UserService;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<BankAccountResponseDto> createAccount(Authentication auth,
                                                                @RequestBody @Valid CreateBankAccountRequestDto request
            ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.createAccount(userService.getAuthenticatedUser(auth), request));
    }

    @GetMapping
    public ListBankAccountsResponseDto listAccounts(Authentication auth) {
        return accountService.listAccounts(userService.getAuthenticatedUser(auth));
    }

    @GetMapping("/{accountNumber}")
    public BankAccountResponseDto fetchAccount(Authentication auth,
                                               @PathVariable String accountNumber) {
        return accountService.fetchAccount(userService.getAuthenticatedUser(auth), accountNumber);
    }

    @PatchMapping("/{accountNumber}")
    public BankAccountResponseDto updateAccount(Authentication auth,
                                                @PathVariable String accountNumber,
                                                @RequestBody UpdateBankAccountRequestDto request) {
        return accountService.updateAccount(userService.getAuthenticatedUser(auth), accountNumber, request);
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(Authentication auth,
                                              @PathVariable String accountNumber) {
        accountService.deleteAccount(userService.getAuthenticatedUser(auth), accountNumber);
        return ResponseEntity.noContent().build();
    }
}
