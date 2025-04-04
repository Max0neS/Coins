package com.example.coinwallet.dto;

import lombok.Data;
import java.util.List;

@Data
public class CategoryWithTransactionsDTO {
    private Long id;
    private String name;
    private List<TransactionDTO> transactions;
}