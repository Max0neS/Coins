package com.example.coinwallet.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    @NotBlank(message = "У пользователя должно быть имя")
    @Size(max = 20, message = "Имя пользователя слишком большое")
    private String name;
    @Email(message = "Email должен быть валидным")
    @NotBlank(message = "Email не может быть пустым")
    private String email;
    private int balance;
}