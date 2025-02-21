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
   * Это поле обязательно и не может быть null.
   */
  private String name;
  /**
     * Дата рождения пользователя.
     * Это поле может быть null, если дата рождения не указана.
     */
  private LocalDate dateOfBirth;
  /**
     * Баланс, связанный с пользователем.
     * Поле представляет текущий баланс пользователя.
     */
  private int balance;
}
