package com.eagle_bank_api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserRequestDto {

    @NotBlank
    private String name;

    @NotNull
    private AddressDto address;

    @NotBlank
    private String phoneNumber;

    @Email
    @NotBlank
    private String email;
}
