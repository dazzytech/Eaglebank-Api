package com.eagle_bank_api.integration.helper.factory;

import com.eagle_bank_api.model.entity.User;

public class UserTestFactory {
    public static User create(String userId) {
        User user = new User();
        user.setId(userId);   // force ID
        user.setName("Test User");
        user.setEmail("test@example.com");
        return user;
    }
}
