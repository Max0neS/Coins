package com.example.coinwallet.controller;

import com.example.coinwallet.dto.TransactionCreateDTO;
import com.example.coinwallet.dto.TransactionDTO;
import com.example.coinwallet.dto.TransactionWithUserAndCategoriesDTO;
import com.example.coinwallet.utils.TransactionCache;
import com.example.coinwallet.service.TransactionService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/transactions")
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final ModelMapper modelMapper;
    private final TransactionCache transactionCache;

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionCreateDTO transactionDTO) {
        TransactionDTO createdTransaction = transactionService.createTransaction(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionDTO>> findAllByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.findAllByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionCreateDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, transactionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/with-details")
    public ResponseEntity<List<TransactionWithUserAndCategoriesDTO>> getAllTransactionsWithDetails() {
        List<TransactionWithUserAndCategoriesDTO> transactions =
                transactionService.getAllTransactionsWithUserAndCategories();
        return ResponseEntity.ok(transactions);
    }

    // GET-запрос с фильтрацией через JPQL
    @GetMapping("/filter/jpql")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByUserIdAndTypeJPQL(
            @RequestParam Long userId,
            @RequestParam boolean type) {
        List<TransactionDTO> transactions = transactionService.findByUserIdAndTypeJPQL(userId, type).stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .toList();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/filter/native")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByUserIdAndTypeNative(
            @RequestParam Long userId,
            @RequestParam boolean type) {
        List<TransactionDTO> transactions = transactionService.findByUserIdAndTypeNative(userId, type).stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .toList();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/clear-cache")
    public ResponseEntity<String> clearCache() {
        transactionCache.clearCache();
        return ResponseEntity.ok("Cache cleared");
    }

}