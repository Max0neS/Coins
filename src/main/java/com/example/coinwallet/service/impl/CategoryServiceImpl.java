package com.example.coinwallet.service.impl;

import com.example.coinwallet.dto.CategoryDTO;
import com.example.coinwallet.dto.CategoryWithTransactionsDTO;
import com.example.coinwallet.exception.ResourceNotFoundException;
import com.example.coinwallet.model.Category;
import com.example.coinwallet.repository.CategoryRepository;
import com.example.coinwallet.service.CategoryService;
import com.example.coinwallet.utils.InMemoryCache;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    private final InMemoryCache cache; // NEW
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class); // NEW

    private static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found with id: ";
    private static final String CATEGORY_ALREADY_EXISTS_MESSAGE = "Category with this name already exists";

    @Override
    @Transactional
    public CategoryDTO createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException(CATEGORY_ALREADY_EXISTS_MESSAGE);
        }
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND_MESSAGE + id));
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryWithTransactionsDTO findCategoryWithTransactions(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND_MESSAGE + id));
        return modelMapper.map(category, CategoryWithTransactionsDTO.class);
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList(); // Изменено здесь
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND_MESSAGE + id));

        if (!existingCategory.getName().equals(category.getName())
                && categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException(CATEGORY_ALREADY_EXISTS_MESSAGE);
        }

        existingCategory.setName(category.getName());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND_MESSAGE + id));

        category.getTransactions().forEach(transaction -> {
            Long userId = transaction.getUser().getId();
            cache.remove(userId);
            LOGGER.info("Invalidated cache for userId: {} due to category deletion", userId);
        });

        category.getTransactions().forEach(transaction ->
                transaction.getCategories().remove(category));

        categoryRepository.delete(category);
        LOGGER.info("Deleted category with id: {}", id);
    }
}