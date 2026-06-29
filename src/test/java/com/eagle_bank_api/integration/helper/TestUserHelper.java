package com.eagle_bank_api.integration.helper;

import com.eagle_bank_api.integration.helper.factory.UserTestFactory;
import com.eagle_bank_api.model.entity.User;
import com.eagle_bank_api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@AllArgsConstructor
public class TestUserHelper {

    public static final String TEST_USER_ID = "test-user-id";
    public static final String TEST_OTHER_USER_ID = "other-user-id";

    private final UserRepository userRepository;

    public String createAuthenticatedTestUserId() {

        return createAuthenticatedTestUser().getId();
    }

    public User createAuthenticatedTestUser() {

        return userRepository.findById(TEST_USER_ID)
                .orElseGet(() -> {
                    return userRepository.save(UserTestFactory.create(TEST_USER_ID));
                });
    }

    public String createOtherUserId() {
        return createOtherUser().getId();
    }

    public User createOtherUser() {
        return userRepository.findById(TEST_OTHER_USER_ID)
                .orElseGet(() -> {
                    return userRepository.save(UserTestFactory.create(TEST_OTHER_USER_ID));
                });
    }

    public void deleteUsers() {
        userRepository.deleteAll();
    }
}
