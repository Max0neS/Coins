package com.example.coinwallet.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserWithTransactionsDTO {
    private Long id;
    private String name;
    private String email;
    private int balance;
    private List<TransactionDTO> transactions;
}