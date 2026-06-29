package com.eagle_bank_api.model.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class UserResponseDto {
    private String id;
    private String name;
    private AddressDto address;
    private String phoneNumber;
    private String email;
    private Instant createdTimestamp;
    private Instant updatedTimestamp;
}