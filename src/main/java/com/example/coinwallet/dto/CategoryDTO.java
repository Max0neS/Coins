package com.example.coinwallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Schema(description = "Сущность категории")
@Data
public class CategoryDTO {

    private Long id;
    @NotBlank(message = "У категории должно быть название")
    @Size(max = 20, message = "Длина названия слишком большая")
    private String name;
}