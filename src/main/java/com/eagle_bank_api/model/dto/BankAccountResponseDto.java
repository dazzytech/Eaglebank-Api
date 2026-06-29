package com.eagle_bank_api.model.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class BankAccountResponseDto {
    private String accountNumber;
    private String sortCode;
    private String name;
    private String accountType;
    private double balance;
    private String currency;
    private Instant createdTimestamp;
    private Instant updatedTimestamp;
}