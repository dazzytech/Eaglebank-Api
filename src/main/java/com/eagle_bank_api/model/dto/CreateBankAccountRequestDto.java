package com.eagle_bank_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBankAccountRequestDto {

    @NotBlank
    private String name;

    @NotNull
    private String accountType;
}