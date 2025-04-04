package com.example.coinwallet.dto;

import lombok.Data;
import java.util.List;

@Data
public class TransactionCreateDTO {
    private String name;
    private String description;
    private boolean type;
    private int amount;
    private Long userId;
    private List<Long> categoryIds;
}