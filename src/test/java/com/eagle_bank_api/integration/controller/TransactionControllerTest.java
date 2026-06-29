package com.eagle_bank_api.integration.controller;

import com.eagle_bank_api.integration.BaseIntegrationTest;
import com.eagle_bank_api.integration.helper.TestAccountHelper;
import com.eagle_bank_api.integration.helper.TestTransactionHelper;
import com.eagle_bank_api.integration.helper.TestUserHelper;
import com.eagle_bank_api.model.entity.BankAccount;
import com.eagle_bank_api.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TransactionControllerTest extends BaseIntegrationTest {

    private User user;
    private BankAccount account;
    @Autowired
    private TestAccountHelper testAccountHelper;
    @Autowired
    private TestTransactionHelper testTransactionHelper;
    @Autowired
    private TestUserHelper testUserHelper;

    @BeforeEach
    void setup() {
        user = testUserHelper.createAuthenticatedTestUser();
        account = testAccountHelper.createTestPersonalAccountForUser(user);
    }

    @Test
    void deposit_success() throws Exception {
        mockMvc.perform(post("/v1/accounts/" + account.getAccountNumber() + "/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "amount": 100.00,
                      "currency": "GBP",
                      "type": "deposit"
                    }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(100.00));
    }

    @Test
    void withdraw_self_success() throws Exception {
        testTransactionHelper.createSeedTransactionForAccount(account, user, 500.00);
        mockMvc.perform(post("/v1/accounts/" + account.getAccountNumber() + "/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
            {
              "amount": 50.00,
              "currency": "GBP",
              "type": "withdrawal"
            }
            """)).andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.amount").value(50.00));
    }

    @Test
    void withdraw_self_insufficientFunds() throws Exception {
        mockMvc.perform(post("/v1/accounts/" + account.getAccountNumber() + "/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
              "amount": 50.00,
              "currency": "GBP",
              "type": "withdrawal"
            }
            """)).andExpect(status().isUnprocessableContent());
    }

    @Test
    void withdraw_other_forbidden() throws Exception {
        User otherUser = testUserHelper.createOtherUser();
        BankAccount otherAccount = testAccountHelper.createTestPersonalAccountForUser(otherUser);
        mockMvc.perform(post("/v1/accounts/" + otherAccount.getAccountNumber() + "/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
            {
              "amount": 50.00,
              "currency": "GBP",
              "type": "withdrawal"
            }
            """)).andExpect(status().isForbidden());
    }

    @Test
    void withdraw_none_notFound() throws Exception {
        mockMvc.perform(post("/v1/accounts/xxxx/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
            {
              "amount": 50.00,
              "currency": "GBP",
              "type": "withdrawal"
            }
            """)).andExpect(status().isNotFound());
    }

    @Test
    void deposit_missing_badRequest() throws Exception {
        mockMvc.perform(post("/v1/accounts/" + account.getAccountNumber() + "/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
            {
              "amount": 50.00,
              "type": "deposit"
            }
            """)).andExpect(status().isBadRequest());
    }

    @Test
    void listTransactions_withPagination_returnsPageOf5() throws Exception {
        testTransactionHelper.createSeedTransactionForAccount(account, user, 1000.00);

        testTransactionHelper.createRandomSequenceTransactionsForAccount(account, user, 10);

        mockMvc.perform(get("/v1/accounts/" + account.getAccountNumber() + "/transactions")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions.length()").value(5));
    }

    @Test
    void listTransactions_other_forbidden() throws Exception {
        User otherUser = testUserHelper.createOtherUser();
        BankAccount otherAccount = testAccountHelper.createTestPersonalAccountForUser(otherUser);
        testTransactionHelper.createSeedTransactionForAccount(otherAccount, otherUser, 1000.00);

        testTransactionHelper.createRandomSequenceTransactionsForAccount(otherAccount, otherUser, 10);

        mockMvc.perform(get("/v1/accounts/" + otherAccount.getAccountNumber() + "/transactions")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isForbidden());
    }

    @Test
    void listTransactions_none_notFound() throws Exception {
        mockMvc.perform(get("/v1/accounts/xxxx/transactions")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void fetch_self_success() throws Exception {
        testTransactionHelper.createSeedTransactionForAccount(account, user, 500.00);

        mockMvc.perform(get("/v1/accounts/" + account.getAccountNumber() + "/transactions/seed_tx"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.amount").value(500.00));
    }

    @Test
    void fetch_other_forbidden() throws Exception {
        User otherUser = testUserHelper.createOtherUser();
        BankAccount otherAccount = testAccountHelper.createTestPersonalAccountForUser(otherUser);
        testTransactionHelper.createSeedTransactionForAccount(otherAccount, otherUser, 1000.00);

        mockMvc.perform(get("/v1/accounts/" + otherAccount.getAccountNumber() + "/transactions/seed_tx"))
                .andExpect(status().isForbidden());
    }

    @Test
    void fetch_noneAccount_notFound() throws Exception {
        testTransactionHelper.createSeedTransactionForAccount(account, user, 500.00);

        mockMvc.perform(get("/v1/accounts/xxxx/transactions/seed_tx"))
                .andExpect(status().isNotFound());
    }

    @Test
    void fetch_noneTransaction_notFound() throws Exception {
        mockMvc.perform(get("/v1/accounts/" + account.getAccountNumber() + "/transactions/xxx"))
                .andExpect(status().isNotFound());
    }

    @Test
    void fetch_wrongAccount_forbidden() throws Exception {
        User otherUser = testUserHelper.createOtherUser();
        BankAccount otherAccount = testAccountHelper.createTestPersonalAccountForUser(otherUser);
        testTransactionHelper.createSeedTransactionForAccount(otherAccount, otherUser, 1000.00);

        mockMvc.perform(get("/v1/accounts/" + account.getAccountNumber() + "/transactions/seed_tx"))
                .andExpect(status().isForbidden());
    }
}
