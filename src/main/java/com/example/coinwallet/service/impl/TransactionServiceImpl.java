package com.example.coinwallet.service.impl;

import com.example.coinwallet.dto.TransactionCreateDTO;
import com.example.coinwallet.dto.TransactionDTO;
import com.example.coinwallet.dto.TransactionWithUserAndCategoriesDTO;
import com.example.coinwallet.exception.ResourceNotFoundException;
import com.example.coinwallet.model.Category;
import com.example.coinwallet.model.Transaction;
import com.example.coinwallet.model.User;
import com.example.coinwallet.repository.CategoryRepository;
import com.example.coinwallet.repository.TransactionRepository;
import com.example.coinwallet.repository.UserRepository;
import com.example.coinwallet.utils.TransactionCache;
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
    private final TransactionCache transactionCache;

    private static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found with id: ";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found with id: ";
    private static final String TRANSACTION_NOT_FOUND_MESSAGE = "Transaction not found with id: ";

    @Override
    @Transactional
    public TransactionDTO createTransaction(TransactionCreateDTO transactionDTO) {
        User user = userRepository.findById(transactionDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + transactionDTO.getUserId()));

        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
        transaction.setUser(user);

        Set<Category> categories = getCategories(transactionDTO.getCategoryIds());

        transaction.setCategories(categories);

        user.updateBalance(transaction.getAmount(), transaction.isType());
        userRepository.save(user);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionDTO.class);
    }

    @Override
    public TransactionDTO findById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TRANSACTION_NOT_FOUND_MESSAGE + id));
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    @Override
    public List<TransactionDTO> findAllByUserId(Long userId) {
        return transactionRepository.findByUserId(userId).stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .toList(); // Изменено здесь
    }

    @Override
    @Transactional
    public TransactionDTO updateTransaction(Long id, TransactionCreateDTO transactionDTO) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TRANSACTION_NOT_FOUND_MESSAGE + id));

        User user = userRepository.findById(transactionDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + transactionDTO.getUserId()));

        Set<Category> categories = getCategories(transactionDTO.getCategoryIds());

        user.updateBalance(-existingTransaction.getAmount(), existingTransaction.isType());

        existingTransaction.setName(transactionDTO.getName());
        existingTransaction.setDescription(transactionDTO.getDescription());
        existingTransaction.setType(transactionDTO.isType());
        existingTransaction.setAmount(transactionDTO.getAmount());
        existingTransaction.setUser(user);
        existingTransaction.setCategories(categories);

        user.updateBalance(existingTransaction.getAmount(), existingTransaction.isType());
        userRepository.save(user);

        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return modelMapper.map(updatedTransaction, TransactionDTO.class);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TRANSACTION_NOT_FOUND_MESSAGE + id));

        User user = transaction.getUser();
        user.updateBalance(-transaction.getAmount(), transaction.isType());
        userRepository.save(user);

        // Удаляем запись из кэша
        String cacheKey = user.getId() + ":" + transaction.isType();
        transactionCache.removeTransactions(cacheKey);

        transactionRepository.delete(transaction);
    }

    @Override
    public List<TransactionWithUserAndCategoriesDTO> getAllTransactionsWithUserAndCategories() {
        List<Transaction> transactions = transactionRepository.findAllWithUserAndCategories();

        return transactions.stream()
                .map(this::convertToDto)
                .toList(); // Изменено здесь
    }

    private TransactionWithUserAndCategoriesDTO convertToDto(Transaction transaction) {
        TransactionWithUserAndCategoriesDTO dto = modelMapper.map(transaction, TransactionWithUserAndCategoriesDTO.class);
        dto.setUserId(transaction.getUser().getId());
        dto.setUserName(transaction.getUser().getName());
        return dto;
    }

    private Set<Category> getCategories(List<Long> categoryIds) {
        Set<Category> categories = new HashSet<>();
        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND_MESSAGE + categoryId));
            categories.add(category);
        }
        return categories;
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> findByUserIdAndTypeJPQL(Long userId, boolean type) {
        String cacheKey = userId + ":" + type; // Формируем ключ
        List<Transaction> cachedTransactions = transactionCache.getTransactions(cacheKey);
        if (cachedTransactions != null) {
            return cachedTransactions;
        }

        List<Transaction> transactions = transactionRepository.findByUserIdAndTypeJPQL(userId, type);
        transactionCache.putTransactions(cacheKey, transactions);
        return transactions;
    }

    @Override
    public List<Transaction> findByUserIdAndTypeNative(Long userId, boolean type) {
        String cacheKey = userId + ":" + type; // Формируем ключ
        List<Transaction> cachedTransactions = transactionCache.getTransactions(cacheKey);
        if (cachedTransactions != null) {
            return cachedTransactions;
        }

        List<Transaction> transactions = transactionRepository.findByUserIdAndTypeNative(userId, type);
        transactionCache.putTransactions(cacheKey, transactions);
        return transactions;
    }
}