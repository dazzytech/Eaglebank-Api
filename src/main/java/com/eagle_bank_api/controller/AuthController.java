package com.eagle_bank_api.controller;

import com.eagle_bank_api.model.dto.TokenRequestDto;
import com.eagle_bank_api.model.dto.TokenResponseDto;
import com.eagle_bank_api.model.entity.User;
import com.eagle_bank_api.repository.UserRepository;
import com.eagle_bank_api.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;

    @PostMapping("/token")
    public ResponseEntity<?> login(@RequestBody TokenRequestDto request) {
        String token = jwtService.generateToken(request.userId());
        return ResponseEntity.ok(new TokenResponseDto(token));
    }
}
