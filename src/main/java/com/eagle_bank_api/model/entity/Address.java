package com.eagle_bank_api.model.entity;


import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {
    private String line1;
    private String line2;
    private String line3;
    private String town;
    private String county;
    private String postcode;
}