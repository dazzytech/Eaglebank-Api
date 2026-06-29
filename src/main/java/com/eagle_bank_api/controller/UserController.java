package com.eagle_bank_api.controller;

import com.eagle_bank_api.model.dto.CreateUserRequestDto;
import com.eagle_bank_api.model.dto.UpdateUserRequestDto;
import com.eagle_bank_api.model.dto.UserResponseDto;
import com.eagle_bank_api.service.UserService;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @RequestBody @Valid CreateUserRequestDto request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }

    @GetMapping("/{userId}")
    public UserResponseDto fetchUser(Authentication auth,
                                     @PathVariable String userId
    ) {
        return userService.fetchUser(userService.getAuthenticatedUser(auth), userId);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(Authentication auth,
                                      @PathVariable String userId,
                                      @RequestBody UpdateUserRequestDto request) {
        return userService.updateUser(userService.getAuthenticatedUser(auth), userId, request);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(Authentication auth,
                                            @PathVariable String userId) {
        userService.deleteUser(userService.getAuthenticatedUser(auth), userId);
        return ResponseEntity.noContent().build();
    }
}
