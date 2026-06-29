package com.eagle_bank_api.model.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserRequestDto {
    private String name;
    private AddressDto address;
    private String phoneNumber;
    @Email
    private String email;
}