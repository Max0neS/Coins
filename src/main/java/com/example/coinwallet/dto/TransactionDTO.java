package com.example.coinwallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Schema(description = "Сущность транзакции")
@Data
public class TransactionDTO {
    private Long id;
    private String name;
    private String description;
    private boolean type;
    private int amount;
    private List<CategoryDTO> categories;
}