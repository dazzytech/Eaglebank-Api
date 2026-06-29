package com.eagle_bank_api.service;


import com.eagle_bank_api.mapper.UserMapper;
import com.eagle_bank_api.model.dto.CreateUserRequestDto;
import com.eagle_bank_api.model.dto.UpdateUserRequestDto;
import com.eagle_bank_api.model.dto.UserResponseDto;
import com.eagle_bank_api.model.entity.User;
import com.eagle_bank_api.repository.AccountRepository;
import com.eagle_bank_api.repository.UserRepository;
import com.eagle_bank_api.exception.ForbiddenException;
import com.eagle_bank_api.exception.NotFoundException;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final UserMapper userMapper;

    public User getAuthenticatedUser(Authentication auth) {
        if (auth == null || auth.getPrincipal() == null) {
            throw new ForbiddenException("missing or invalid JWT");
        }

        String userId = (String) auth.getPrincipal();

        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public UserResponseDto createUser(CreateUserRequestDto request) {
        User user = userMapper.toEntity(request);
        user.setId("usr-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8));
        user.setCreatedTimestamp(Instant.now());
        user.setUpdatedTimestamp(Instant.now());
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public UserResponseDto fetchUser(User user, String userId) {
        if(userRepository.findById(userId).isEmpty())
            throw new NotFoundException("User is not found");
        if(!user.getId().equals(userId))
            throw new ForbiddenException("Cannot fetch another user");

        return userMapper.toResponse(user);
    }

    public UserResponseDto updateUser(User user, String userId, UpdateUserRequestDto request) {
        if(userRepository.findById(userId).isEmpty())
            throw new NotFoundException("User is not found");
        if(!user.getId().equals(userId))
            throw new ForbiddenException("Cannot update the details of another user");

        if (request.getName() != null) user.setName(request.getName());
        if (request.getAddress() != null) user.setAddress(userMapper.toEntity(request.getAddress()));
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getEmail() != null) user.setEmail(request.getEmail());

        user.setUpdatedTimestamp(Instant.now());
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public void deleteUser(User user, String userId) {
        if(userRepository.findById(userId).isEmpty())
            throw new NotFoundException("User is not found");
        if(!user.getId().equals(userId))
            throw new ForbiddenException("Cannot delete another user");

        if (!accountRepository.findByUser(user).isEmpty()) {
            throw new ForbiddenException("User cannot be deleted when associated with a bank account");
        }

        userRepository.delete(user);
    }
}