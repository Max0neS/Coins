package com.example.coinwallet.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransactionWithCategoriesDTO {
    private Long id;
    private String name;
    private String description;
    private boolean type;
    private int amount;
    private List<CategoryDTO> categories;
}
