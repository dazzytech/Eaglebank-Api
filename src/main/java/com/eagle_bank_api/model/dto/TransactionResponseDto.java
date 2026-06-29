package com.eagle_bank_api.model.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class TransactionResponseDto {
    private String id;
    private double amount;
    private String currency;
    private String type;
    private String reference;
    private String userId;
    private Instant createdTimestamp;
}