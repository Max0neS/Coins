package com.example.coinwallet.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class User {

    @NonNull
    private String name;

    private LocalDate dateOfBirth;

    private int balance;
}
