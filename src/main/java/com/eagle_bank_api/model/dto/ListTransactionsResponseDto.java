package com.eagle_bank_api.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ListTransactionsResponseDto {
    private List<TransactionResponseDto> transactions;
}