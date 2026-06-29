package com.eagle_bank_api.model.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTransactionRequestDto {

    @DecimalMin("0.00")
    private double amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String type;

    private String reference;
}