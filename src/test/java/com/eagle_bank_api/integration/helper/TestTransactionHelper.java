package com.eagle_bank_api.integration.helper;

import com.eagle_bank_api.integration.helper.factory.TransactionTestFactory;
import com.eagle_bank_api.model.entity.BankAccount;
import com.eagle_bank_api.model.entity.Transaction;
import com.eagle_bank_api.model.entity.TransactionType;
import com.eagle_bank_api.model.entity.User;
import com.eagle_bank_api.repository.AccountRepository;
import com.eagle_bank_api.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@Profile("test")
@AllArgsConstructor
public class TestTransactionHelper {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    private final String SEED_ID = "seed_tx";

    public void createSeedTransactionForAccount(BankAccount account,
                                                User user,
                                                Double amount) {
        transactionRepository.save(TransactionTestFactory.createDeposit(user, account, amount, SEED_ID));
        updateBalance(account, amount);
    }

    public void createRandomSequenceTransactionsForAccount(BankAccount account,
                                                           User user,
                                                           Integer numberOfTransactions)
    {
        for(int i = 0; i < numberOfTransactions; i++) {
            Double amount = i * 10 + 0.00;
            if(i % 2 == 0) {
                transactionRepository.save(TransactionTestFactory.createDeposit(user, account, amount,
                        UUID.randomUUID().toString()));
                updateBalance(account, amount);
            } else {
                transactionRepository.save(TransactionTestFactory.createWithdrawal(user, account, amount,
                        UUID.randomUUID().toString()));
                updateBalance(account, -amount);
            }
        }
    }

    private Double updateBalance(BankAccount bankAccount, Double change) {
        bankAccount.setBalance(Math.max(bankAccount.getBalance() + change, 0.00));
        bankAccount.setUpdatedTimestamp(Instant.now());
        return accountRepository.save(bankAccount).getBalance();
    }

    public void deleteTransactions() {
        transactionRepository.deleteAll();;
    }
}
