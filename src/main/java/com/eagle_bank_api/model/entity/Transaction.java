package com.eagle_bank_api.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private String id;

    private double amount;

    private String currency;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String reference;

    private Instant createdTimestamp;

    @ManyToOne
    private BankAccount account;

    @ManyToOne
    private User user;

    //TODO: add a mandate number to determine the payee, also may need to create a mandate table
}
