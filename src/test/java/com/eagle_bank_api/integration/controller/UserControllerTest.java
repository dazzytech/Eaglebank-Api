package com.eagle_bank_api.integration.controller;

import com.eagle_bank_api.integration.BaseIntegrationTest;
import com.eagle_bank_api.integration.helper.TestAccountHelper;
import com.eagle_bank_api.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest extends BaseIntegrationTest {

    @Autowired
    private TestAccountHelper testAccountHelper;

    @Test
    void createUser_success() throws Exception {
        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "Alice",
                      "address": {
                        "line1": "1 Road",
                        "town": "Town",
                        "county": "County",
                        "postcode": "AA1 1AA"
                      },
                      "phoneNumber": "+441234567890",
                      "email": "alice@example.com"
                    }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void createUser_missingFields_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "Alice"
                    }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void fetchUser_self_success() throws Exception {
        String userId = testUserHelper.createAuthenticatedTestUserId();

        mockMvc.perform(get("/v1/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId));
    }

    @Test
    void fetchUser_other_Forbidden() throws Exception {
        testUserHelper.createAuthenticatedTestUserId();
        String otherId = testUserHelper.createOtherUserId();

        mockMvc.perform(get("/v1/users/" + otherId))
                .andExpect(status().isForbidden());
    }

    @Test
    void fetchUser_notFound() throws Exception {
        testUserHelper.createAuthenticatedTestUserId();
        mockMvc.perform(get("/v1/users/usr-xxxx"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_success() throws Exception {
        String userId = testUserHelper.createAuthenticatedTestUserId();
        mockMvc.perform(patch("/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "Updated Name"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void updateOtherUser_forbidden() throws Exception {
        testUserHelper.createAuthenticatedTestUserId();
        String otherId = testUserHelper.createOtherUserId();
        mockMvc.perform(patch("/v1/users/" + otherId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "Updated Name"
                    }
                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateUser_notFound() throws Exception {
        testUserHelper.createAuthenticatedTestUserId();
        mockMvc.perform(patch("/v1/users/usr-xxxx")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    { "name": "X" }
                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_success_whenNoAccounts() throws Exception {
        String userId = testUserHelper.createAuthenticatedTestUserId();

        mockMvc.perform(delete("/v1/users/" + userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_forbidden_whenHasAccounts() throws Exception {
        User user = testUserHelper.createAuthenticatedTestUser();

        testAccountHelper.createTestPersonalAccountForUser(user);

        mockMvc.perform(delete("/v1/users/" + user.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUser_forbidden_whenNotAuthUser() throws Exception {
        testUserHelper.createAuthenticatedTestUser();
        String otherId = testUserHelper.createOtherUserId();
        mockMvc.perform(delete("/v1/users/" + otherId))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUser_notFound_whenUserDoesntExist() throws Exception {
        testUserHelper.createAuthenticatedTestUser();

        mockMvc.perform(delete("/v1/users/user-xxxx"))
                .andExpect(status().isNotFound());
    }
}
