package com.example.coinwallet.service;

import com.example.coinwallet.dto.CategoryDTO;
import com.example.coinwallet.dto.CategoryWithTransactionsDTO;
import com.example.coinwallet.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(Category category);

    CategoryDTO findById(Long id);

    CategoryWithTransactionsDTO findCategoryWithTransactions(Long id);

    List<CategoryDTO> findAll();

    CategoryDTO updateCategory(Long id, Category category);

    void deleteCategory(Long id);
}