    package com.example.coinwallet.controller;

    import com.example.coinwallet.dto.CategoryDTO;
    import com.example.coinwallet.dto.CategoryWithTransactionsDTO;
    import com.example.coinwallet.model.Category;
    import com.example.coinwallet.service.CategoryService;
    import io.swagger.v3.oas.annotations.Operation;
    import lombok.AllArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import io.swagger.v3.oas.annotations.tags.Tag;

    import java.util.List;

    @Tag(name = "Контроллер КАТЕГОРИЙ", description = "позволяет работать с категориями")
    @RestController
    @RequestMapping("/categories")
    @AllArgsConstructor
    public class CategoryController {
        private final CategoryService categoryService;

        @Operation(
                summary = "Создание категории",
                description = "Позволяет создать общую категорию"
        )
        @PostMapping("/create")
        public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category) {
            CategoryDTO createdCategory = categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        }

        @Operation(
                summary = "Вывод всех категорий",
                description = "Позволяет просмотреть все существующие категории"
        )
        @GetMapping("/get-all")
        public List<CategoryDTO> findAll() {
            return categoryService.findAll();
        }

        @Operation(
                summary = "Поиск категории по ID",
                description = "Позволяет найти категорию по ее ID"
        )
        @GetMapping("/get-by-id/{id}")
        public CategoryDTO findById(@PathVariable Long id) {
            return categoryService.findById(id);
        }

        @Operation(
                summary = "Вывод всех транзакций данной категории",
                description = "Показывает все транзакции которые привязаны к данной категории"
        )
        @GetMapping("/show-category-transactions-by-id/{id}")
        public CategoryWithTransactionsDTO findCategoryWithTransactions(@PathVariable Long id) {
            return categoryService.findCategoryWithTransactions(id);
        }

        @Operation(
                summary = "Обновление категории по ID",
                description = "Позволяет изменить имя категории"
        )
        @PutMapping("/update-by-id/{id}")
        public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody Category category) {
            return ResponseEntity.ok(categoryService.updateCategory(id, category));
        }

        @Operation(
                summary = "Удаление категории по ID",
                description = "Позволяет удалить категорию"
        )
        @DeleteMapping("/del-by-id/{id}")
        public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        }
    }