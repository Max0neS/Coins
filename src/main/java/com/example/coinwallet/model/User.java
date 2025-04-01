package com.example.coinwallet.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

/**
 * Представляет пользователя с личными данными и балансом.
 */
@Data
@Entity
@Table(name = "myUsers")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private LocalDate dateOfBirth;
    private int balance;
}
