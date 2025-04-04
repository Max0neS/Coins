package com.example.coinwallet.service.impl;

import com.example.coinwallet.dto.CategoryDTO;
import com.example.coinwallet.dto.CategoryWithTransactionsDTO;
import com.example.coinwallet.exception.ResourceNotFoundException;
import com.example.coinwallet.model.Category;
import com.example.coinwallet.repository.CategoryRepository;
import com.example.coinwallet.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CategoryDTO createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category with this name already exists");
        }
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryWithTransactionsDTO findCategoryWithTransactions(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return modelMapper.map(category, CategoryWithTransactionsDTO.class);
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!existingCategory.getName().equals(category.getName()) &&
                categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category with this name already exists");
        }

        existingCategory.setName(category.getName());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        // Удаляем связи с транзакциями перед удалением категории
        category.getTransactions().forEach(transaction ->
                transaction.getCategories().remove(category));

        categoryRepository.delete(category);
    }
}