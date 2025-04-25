package com.example.coinwallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Schema(description = "Сущность транзакции")
@Data
public class TransactionDTO {
    private Long id;
    @NotBlank(message = "У транзакции должно быть название")
    @Size(max = 20, message = "Длина названия слишком большая")
    private String name;
    private String description;
    @NotNull(message = "Тип транзакции должен быть указан")
    private boolean type;
    @Min(value = 0, message = "Сумма транзакции не может быть меньше 0")
    private int amount;
    private List<CategoryDTO> categories;
}