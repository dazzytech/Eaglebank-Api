package com.eagle_bank_api.model.dto;

import lombok.Data;

@Data
public class UpdateBankAccountRequestDto {
    private String name;
    private String accountType;
}
