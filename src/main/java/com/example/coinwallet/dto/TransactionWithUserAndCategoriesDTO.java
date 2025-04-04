package com.example.coinwallet.dto;

import lombok.Data;
import java.util.List;

@Data
public class TransactionWithUserAndCategoriesDTO {
    private Long id;
    private String name;
    private String description;
    private boolean type;
    private int amount;
    private Long userId; // ID пользователя
    private String userName; // Имя пользователя (опционально)
    private List<CategoryDTO> categories; // Список категорий
}