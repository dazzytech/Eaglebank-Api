package com.eagle_bank_api.integration.helper.factory;

import com.eagle_bank_api.model.entity.AccountType;
import com.eagle_bank_api.model.entity.BankAccount;
import com.eagle_bank_api.model.entity.User;

import java.time.Instant;

public class AccountTestFactory {
    public static BankAccount createPersonal(User user, String accountId) {
        BankAccount account = new BankAccount();
        account.setAccountType(AccountType.PERSONAL);
        account.setAccountNumber(accountId);
        account.setSortCode("10-10-10");
        account.setCurrency("GBP");
        account.setBalance(0.0);
        account.setUser(user);
        account.setUpdatedTimestamp(Instant.now());
        account.setCreatedTimestamp(Instant.now());
        return account;
    }
}
