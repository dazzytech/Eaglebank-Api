package com.eagle_bank_api.integration;

import com.eagle_bank_api.integration.helper.TestAccountHelper;
import com.eagle_bank_api.integration.helper.TestTransactionHelper;
import com.eagle_bank_api.integration.helper.TestUserHelper;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected TestUserHelper testUserHelper;

    @Autowired
    protected TestAccountHelper testAccountHelper;

    @Autowired
    protected TestTransactionHelper testTransactionHelper;

    @AfterEach
    void cleanUp() {
        testTransactionHelper.deleteTransactions();
        testAccountHelper.deleteAccounts();
        testUserHelper.deleteUsers();
    }
}
