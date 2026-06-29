package com.eagle_bank_api.model.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name = "users")                          // avoid hibernate gotcha of existing table
public class User {

    @Id
    private String id;

    private String name;

    @Embedded
    private Address address;

    private String phoneNumber;

    private String email;

    private Instant createdTimestamp;
    private Instant updatedTimestamp;
}