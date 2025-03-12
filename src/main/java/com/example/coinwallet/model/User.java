package com.example.coinwallet.model;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

/**
 * Представляет пользователя с личными данными и балансом.
 */
@Data
@Builder
public class User {

    /**
     * Имя пользователя.
     */
    private Integer id;

    /**
       * Имя пользователя.
       */
    private String name;
    /**
       * Дата рождения пользователя.
       */
    private LocalDate dateOfBirth;
    /**
       * Баланс, связанный с пользователем.
       */
    private int balance;
}
