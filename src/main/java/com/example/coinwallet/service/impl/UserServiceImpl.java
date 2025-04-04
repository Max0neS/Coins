package com.example.coinwallet.service.impl;

import com.example.coinwallet.dto.*;
import com.example.coinwallet.exception.ResourceNotFoundException;
import com.example.coinwallet.model.User;
import com.example.coinwallet.repository.UserRepository;
import com.example.coinwallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private static final String USER_NOT_FOUND_MESSAGE = "User not found with id: ";
    private static final String EMAIL_ALREADY_EXISTS_MESSAGE = "Email already exists";

    @Override
    public List<UserDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(EMAIL_ALREADY_EXISTS_MESSAGE);
        }
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + id));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserWithTransactionsDTO findUserWithTransactions(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + id));
        return modelMapper.map(user, UserWithTransactionsDTO.class);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + id));

        if (!existingUser.getEmail().equals(user.getEmail())
                && userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(EMAIL_ALREADY_EXISTS_MESSAGE);
        }

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());

        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + id));
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserWithTransactionsDTO> getAllUsersWithTransactionsAndCategories() {
        List<User> users = userRepository.findAllWithTransactionsAndCategories();

        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserWithTransactionsDTO convertToDto(User user) {
        UserWithTransactionsDTO dto = modelMapper.map(user, UserWithTransactionsDTO.class);

        dto.setTransactions(user.getTransactions().stream()
                .map(t -> {
                    TransactionWithCategoriesDTO tDto =
                            modelMapper.map(t, TransactionWithCategoriesDTO.class);

                    tDto.setCategories(t.getCategories().stream()
                            .map(c -> modelMapper.map(c, CategoryDTO.class))
                            .collect(Collectors.toList()));

                    return tDto;
                })
                .collect(Collectors.toList()));

        return dto;
    }
}