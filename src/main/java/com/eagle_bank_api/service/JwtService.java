package com.eagle_bank_api.service;

import com.eagle_bank_api.exception.NotFoundException;
import com.eagle_bank_api.model.entity.User;
import com.eagle_bank_api.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserRepository userRepository;

    private final SecretKey key = Keys.hmacShaKeyFor(
            "super-secret-key-change-me-super-secret-key".getBytes(StandardCharsets.UTF_8)
    );

    public String generateToken(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return Jwts.builder()
                .subject(user.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .signWith(key)
                .compact();
    }

    public String extractUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }
}
