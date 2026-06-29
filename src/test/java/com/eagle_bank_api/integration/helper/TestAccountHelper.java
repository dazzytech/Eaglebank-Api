package com.eagle_bank_api.integration.helper;

import com.eagle_bank_api.integration.helper.factory.AccountTestFactory;
import com.eagle_bank_api.model.entity.AccountType;
import com.eagle_bank_api.model.entity.BankAccount;
import com.eagle_bank_api.model.entity.User;
import com.eagle_bank_api.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Profile("test")
@AllArgsConstructor
public class TestAccountHelper {

    private final String TEST_ACCOUNT_ID = "test-account-id";

    private final AccountRepository accountRepository;

    public BankAccount createTestPersonalAccountForUser(User user) {

        return accountRepository.findByUser(user).stream().findFirst()
                .orElseGet(() -> {
                    return accountRepository.save(AccountTestFactory.createPersonal(user, TEST_ACCOUNT_ID));
                });
    }

    public void deleteAccounts() {
        accountRepository.deleteAll();
    }
}
