package com.eagle_bank_api.integration.helper.factory;

import com.eagle_bank_api.model.entity.BankAccount;
import com.eagle_bank_api.model.entity.Transaction;
import com.eagle_bank_api.model.entity.TransactionType;
import com.eagle_bank_api.model.entity.User;

import java.time.Instant;

public class TransactionTestFactory {
    public static Transaction createDeposit(User user, BankAccount account, Double amount, String tx_id) {
        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setId(tx_id);
        tx.setUser(user);
        tx.setAmount(amount);
        tx.setReference("Test reference");
        tx.setCurrency("GBP");
        tx.setType(TransactionType.DEPOSIT);
        tx.setCreatedTimestamp(Instant.now());

        return tx;
    }

    public static Transaction createWithdrawal(User user, BankAccount account, Double amount, String tx_id) {
        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setId(tx_id);
        tx.setUser(user);
        tx.setAmount(amount);
        tx.setCurrency("GBP");
        tx.setType(TransactionType.WITHDRAWAL);
        tx.setCreatedTimestamp(Instant.now());

        return tx;
    }
}
