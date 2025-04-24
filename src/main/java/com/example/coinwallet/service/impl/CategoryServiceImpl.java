package com.example.coinwallet.service.impl;

import com.example.coinwallet.dto.CategoryDTO;
import com.example.coinwallet.dto.CategoryWithTransactionsDTO;
import com.example.coinwallet.exception.ResourceNotFoundException;
import com.example.coinwallet.model.Category;
import com.example.coinwallet.repository.CategoryRepository;
import com.example.coinwallet.service.CategoryService;
import com.example.coinwallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

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
        updateUserCacheForCategory(updatedCategory);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND_MESSAGE + id));

        updateUserCacheForCategory(category);

        // Удаляем связи с транзакциями перед удалением категории
        category.getTransactions().forEach(transaction ->
                transaction.getCategories().remove(category));

        categoryRepository.delete(category);
    }

    private void updateUserCacheForCategory(Category category) {
        Set<Long> userIds = category.getTransactions().stream()
                .map(transaction -> transaction.getUser().getId())
                .collect(Collectors.toSet());
        for (Long userId : userIds) {
            userService.updateUserCache(userId);
        }
    }
}