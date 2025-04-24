package com.example.coinwallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Schema(description = "Сущность транзакции при создании")
@Data
public class TransactionCreateDTO {
    private String name;
    private String description;
    private boolean type;
    private int amount;
    private Long userId;
    private List<Long> categoryIds;
}