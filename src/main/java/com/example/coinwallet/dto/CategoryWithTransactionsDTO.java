package com.example.coinwallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Schema(description = "Сущность категории и ее транзакций")
@Data
public class CategoryWithTransactionsDTO {
    private Long id;
    private String name;
    private List<TransactionDTO> transactions;
}