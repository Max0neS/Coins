package com.example.coinwallet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "У пользователя должно быть имя")
    @Size(max = 20, message = "Имя пользователя слишком большое")
    @Column(nullable = false)
    private String name;

    @Email(message = "Email должен быть валидным")
    @NotBlank(message = "Email не может быть пустым")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int balance = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Transaction> transactions = new ArrayList<>();

    public void updateBalance(int amount, boolean isIncome) {
        this.balance += isIncome ? amount : -amount;
    }
}