    package com.example.coinwallet.controller;

    import com.example.coinwallet.dto.CategoryDTO;
    import com.example.coinwallet.dto.CategoryWithTransactionsDTO;
    import com.example.coinwallet.model.Category;
    import com.example.coinwallet.service.CategoryService;
    import lombok.AllArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/v1/categories")
    @AllArgsConstructor
    public class CategoryController {
        private final CategoryService categoryService;

        @PostMapping
        public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category) {
            CategoryDTO createdCategory = categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        }

        @GetMapping
        public ResponseEntity<List<CategoryDTO>> findAll() {
            return ResponseEntity.ok(categoryService.findAll());
        }

        @GetMapping("/{id}")
        public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
            return ResponseEntity.ok(categoryService.findById(id));
        }

        @GetMapping("/{id}/transactions")
        public ResponseEntity<CategoryWithTransactionsDTO> findCategoryWithTransactions(@PathVariable Long id) {
            return ResponseEntity.ok(categoryService.findCategoryWithTransactions(id));
        }

        @PutMapping("/{id}")
        public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody Category category) {
            return ResponseEntity.ok(categoryService.updateCategory(id, category));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        }
    }