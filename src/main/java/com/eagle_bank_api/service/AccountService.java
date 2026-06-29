package com.eagle_bank_api.service;


import com.eagle_bank_api.mapper.AccountMapper;
import com.eagle_bank_api.model.dto.BankAccountResponseDto;
import com.eagle_bank_api.model.dto.CreateBankAccountRequestDto;
import com.eagle_bank_api.model.dto.ListBankAccountsResponseDto;
import com.eagle_bank_api.model.dto.UpdateBankAccountRequestDto;
import com.eagle_bank_api.model.entity.AccountType;
import com.eagle_bank_api.model.entity.BankAccount;
import com.eagle_bank_api.model.entity.User;
import com.eagle_bank_api.repository.AccountRepository;
import com.eagle_bank_api.exception.NotFoundException;
import com.eagle_bank_api.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public BankAccountResponseDto createAccount(User user,
                                                CreateBankAccountRequestDto request) {
        BankAccount account = accountMapper.toEntity(request);
        account.setAccountNumber(generateAccountNumber());
        account.setSortCode("10-10-10");
        account.setCurrency("GBP");
        account.setBalance(0.0);
        account.setCreatedTimestamp(Instant.now());
        account.setUpdatedTimestamp(Instant.now());
        account.setUser(user);

        accountRepository.save(account);
        return accountMapper.toResponse(account);
    }

    public ListBankAccountsResponseDto listAccounts(User user) {
        List<BankAccount> accounts = accountRepository.findByUser(user);
        List<BankAccountResponseDto> list = accounts.stream()
                .map(accountMapper::toResponse)
                .collect(Collectors.toList());
        ListBankAccountsResponseDto resp = new ListBankAccountsResponseDto();
        resp.setAccounts(list);
        return resp;
    }

    public BankAccountResponseDto fetchAccount(User user,
                                               String accountNumber) {
        BankAccount account = getAccount(user, accountNumber);

        return accountMapper.toResponse(account);
    }

    public BankAccountResponseDto updateAccount(User user,
                                                String accountNumber,
                                                UpdateBankAccountRequestDto request) {
        BankAccount account = getAccount(user, accountNumber);

        if (request.getName() != null) account.setName(request.getName());
        // TODO: implement update account type
        account.setUpdatedTimestamp(Instant.now());
        accountRepository.save(account);
        return accountMapper.toResponse(account);
    }

    public void deleteAccount(User user,
                              String accountNumber) {
        BankAccount account = getAccount(user, accountNumber);
        accountRepository.delete(account);
    }

    private String generateAccountNumber() {
        int num = (int) (Math.random() * 1_000_000);
        return String.format("01%06d", num);
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