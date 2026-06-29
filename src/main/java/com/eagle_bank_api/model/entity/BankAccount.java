package com.eagle_bank_api.model.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.time.Instant;

@Data
@Entity
public class BankAccount {

    @Id
    private String accountNumber;

    private String sortCode;

    private String name;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private double balance;

    private String currency;

    private Instant createdTimestamp;
    private Instant updatedTimestamp;

    @ManyToOne
    private User user;
}
