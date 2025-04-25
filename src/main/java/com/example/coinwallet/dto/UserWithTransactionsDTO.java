package com.example.coinwallet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class UserWithTransactionsDTO {
    private Long id;
    @NotBlank(message = "У пользователя должно быть имя")
    @Size(max = 20, message = "Имя пользователя слишком большое")
    private String name;
    @Email(message = "Email должен быть валидным")
    @NotBlank(message = "Email не может быть пустым")
    private String email;
    private int balance;
    private List<TransactionWithCategoriesDTO> transactions;
}