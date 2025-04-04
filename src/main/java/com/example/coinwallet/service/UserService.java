package com.example.coinwallet.service;

import com.example.coinwallet.dto.UserDTO;
import com.example.coinwallet.dto.UserWithTransactionsDTO;
import com.example.coinwallet.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    List<UserDTO> findAllUsers();

    UserDTO saveUser(User user);

    UserDTO findById(Long id);

    UserWithTransactionsDTO findUserWithTransactions(Long id);

    UserDTO updateUser(Long id, User user);

    void deleteUser(Long id);

    @Transactional(readOnly = true)
    List<UserWithTransactionsDTO> getAllUsersWithTransactionsAndCategories();
}