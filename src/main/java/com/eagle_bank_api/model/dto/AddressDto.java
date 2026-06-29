package com.eagle_bank_api.model.dto;

import lombok.Data;

@Data
public class AddressDto {
    private String line1;
    private String line2;
    private String line3;
    private String town;
    private String county;
    private String postcode;
}
