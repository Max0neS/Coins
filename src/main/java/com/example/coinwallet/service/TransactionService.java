package com.example.coinwallet.service;

import com.example.coinwallet.dto.TransactionCreateDTO;
import com.example.coinwallet.dto.TransactionDTO;
import com.example.coinwallet.model.Transaction;

import java.util.List;

public interface TransactionService {
    TransactionDTO createTransaction(TransactionCreateDTO transactionDTO);

    TransactionDTO findById(Long id);

    List<TransactionDTO> findAllByUserId(Long userId);

    TransactionDTO updateTransaction(Long id, TransactionCreateDTO transactionDTO);

    void deleteTransaction(Long id);
}