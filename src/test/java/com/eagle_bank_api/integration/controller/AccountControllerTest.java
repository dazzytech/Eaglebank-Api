package com.eagle_bank_api.integration.controller;
import com.eagle_bank_api.integration.BaseIntegrationTest;
import com.eagle_bank_api.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AccountControllerTest extends BaseIntegrationTest {

    private User user;

    @BeforeEach
    void setup() {
        user = testUserHelper.createAuthenticatedTestUser();
    }

    @Test
    void createAccount_success() throws Exception {
        mockMvc.perform(post("/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "My Account",
                      "accountType": "personal"
                    }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").exists());
    }

    @Test
    void createAccount_missingFields_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    { "name": "Missing type" }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void fetchAccounts_self_success() throws Exception {
        testAccountHelper.createTestPersonalAccountForUser(user);

        mockMvc.perform(get("/v1/accounts"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.accounts").exists());
    }

    @Test
    void fetchAccount_otherUser_forbidden() throws Exception {
        User otherUser = testUserHelper.createOtherUser();
        String accountNumber = testAccountHelper.createTestPersonalAccountForUser(otherUser).getAccountNumber();

        mockMvc.perform(get("/v1/accounts/" + accountNumber))
                .andExpect(status().isForbidden());
    }

    @Test
    void fetchAccount_self_notFound() throws Exception {
        mockMvc.perform(get("/v1/accounts/xxxx"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAccount_self_success() throws Exception {
        String accountNumber = testAccountHelper.createTestPersonalAccountForUser(user).getAccountNumber();

        mockMvc.perform(patch("/v1/accounts/" + accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "Updated Account Name"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Account Name"));
    }

    @Test
    void updateAccount_other_forbidden() throws Exception {
        User otherUser = testUserHelper.createOtherUser();
        String accountNumber = testAccountHelper.createTestPersonalAccountForUser(otherUser).getAccountNumber();

        mockMvc.perform(patch("/v1/accounts/" + accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "Updated Account Name"
                    }
                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateAccount_null_notfound() throws Exception {
        mockMvc.perform(patch("/v1/accounts/xxxx")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "Updated Account Name"
                    }
                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAccount_self_success() throws Exception {
        String accountNumber = testAccountHelper.createTestPersonalAccountForUser(user).getAccountNumber();

        mockMvc.perform(delete("/v1/accounts/" + accountNumber))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAccount_other_forbidden() throws Exception {
        User otherUser = testUserHelper.createOtherUser();
        String accountNumber = testAccountHelper.createTestPersonalAccountForUser(otherUser).getAccountNumber();

        mockMvc.perform(delete("/v1/accounts/" + accountNumber))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteAccount_null_notfound() throws Exception {
        mockMvc.perform(delete("/v1/accounts/xxxx"))
                .andExpect(status().isNotFound());
    }
}