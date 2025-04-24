package com.example.coinwallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "Сущность категории")
@Data
public class CategoryDTO {

    private Long id;
    @NotBlank
    private String name;
}