package com.example.coinwallet.service.impl;

import com.example.coinwallet.dto.TransactionCreateDTO;
import com.example.coinwallet.dto.TransactionDTO;
import com.example.coinwallet.exception.ResourceNotFoundException;
import com.example.coinwallet.model.*;
import com.example.coinwallet.repository.TransactionRepository;
import com.example.coinwallet.repository.UserRepository;
import com.example.coinwallet.repository.CategoryRepository;
import com.example.coinwallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public TransactionDTO createTransaction(TransactionCreateDTO transactionDTO) {
        User user = userRepository.findById(transactionDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + transactionDTO.getUserId()));

        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
        transaction.setUser(user);

        // Очищаем и переустанавливаем категории безопасным способом
        Set<Category> categories = new HashSet<>();
        for (Long categoryId : transactionDTO.getCategoryIds()) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
            categories.add(category);
        }
        transaction.setCategories(categories);

        // Обновляем баланс пользователя
        user.updateBalance(transaction.getAmount(), transaction.isType());
        userRepository.save(user);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionDTO.class);
    }

    // Остальные методы остаются без изменений
    @Override
    public TransactionDTO findById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    @Override
    public List<TransactionDTO> findAllByUserId(Long userId) {
        return transactionRepository.findByUser_Id(userId).stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TransactionDTO updateTransaction(Long id, TransactionCreateDTO transactionDTO) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        User user = userRepository.findById(transactionDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + transactionDTO.getUserId()));

        Set<Category> categories = new HashSet<>();
        for (Long categoryId : transactionDTO.getCategoryIds()) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
            categories.add(category);
        }

        // Восстанавливаем старый баланс
        user.updateBalance(-existingTransaction.getAmount(), existingTransaction.isType());

        existingTransaction.setName(transactionDTO.getName());
        existingTransaction.setDescription(transactionDTO.getDescription());
        existingTransaction.setType(transactionDTO.isType());
        existingTransaction.setAmount(transactionDTO.getAmount());
        existingTransaction.setUser(user);
        existingTransaction.setCategories(categories);

        // Применяем новую транзакцию
        user.updateBalance(existingTransaction.getAmount(), existingTransaction.isType());
        userRepository.save(user);

        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return modelMapper.map(updatedTransaction, TransactionDTO.class);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        User user = transaction.getUser();
        user.updateBalance(-transaction.getAmount(), transaction.isType());
        userRepository.save(user);

        transactionRepository.delete(transaction);
    }
}