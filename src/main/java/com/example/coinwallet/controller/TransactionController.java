package com.example.coinwallet.controller;

import com.example.coinwallet.dto.TransactionCreateDTO;
import com.example.coinwallet.dto.TransactionDTO;
import com.example.coinwallet.utils.InMemoryCache;
import com.example.coinwallet.dto.TransactionWithUserAndCategoriesDTO;
import com.example.coinwallet.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Контроллер ТРАНЗАКЦИЙ", description = "позволяет работать с транзакциями")
@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final InMemoryCache cache;

    @Operation(
            summary = "Создание транзакции",
            description = "Позволяет создать транзакцию"
    )
    @PostMapping("/create")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionCreateDTO transactionDTO) {
        TransactionDTO createdTransaction = transactionService.createTransaction(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @Operation(
            summary = "Поиск транзакции по ID",
            description = "Позволяет найти транзакцию по ID"
    )
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<TransactionDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }

    @Operation(
            summary = "Поиск всех транзакций пользователя по ID",
            description = "Позволяет найти все транзакции пользователя"
    )
    @GetMapping("/get-user-transactions/{userId}")
    public ResponseEntity<List<TransactionDTO>> findAllByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.findAllByUserId(userId));
    }

    @Operation(
            summary = "Обновление транзакции по ID",
            description = "Позволяет обновить транзакцию"
    )
    @PutMapping("/update-by-id/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionCreateDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, transactionDTO));
    }

    @Operation(
            summary = "Удаление транзакции по ID",
            description = "Позволяет удалить транзакцию"
    )
    @DeleteMapping("/del-by-id/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Вывод всех транзакций с их категориями",
            description = "Позволяет просмотреть транзакции и их категории"
    )
    @GetMapping("/show-with-details")
    public ResponseEntity<List<TransactionWithUserAndCategoriesDTO>> getAllTransactionsWithDetails() {
        List<TransactionWithUserAndCategoriesDTO> transactions =
                transactionService.getAllTransactionsWithUserAndCategories();
        return ResponseEntity.ok(transactions);
    }

    @Operation(
            summary = "Вывод всех транзакций категории",
            description = "Позволяет просмотреть транзакции категории"
    )
    @GetMapping("/show-category-transactions")
    public ResponseEntity<List<TransactionWithUserAndCategoriesDTO>> findByCategoryIds(@RequestParam List<Long> categoryIds) {
        return ResponseEntity.ok(transactionService.findByCategoryIds(categoryIds));
    }

    @Operation(
            summary = "Очистка кеша транзакций",
            description = "Позволяет осчистить кеш"
    )
    @PostMapping("/cache/clear")
    public ResponseEntity<Void> clearCache() {
        cache.clear();
        return ResponseEntity.noContent().build();
    }

}