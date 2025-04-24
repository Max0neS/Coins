package com.example.coinwallet.service.impl;

import com.example.coinwallet.dto.*;
import com.example.coinwallet.exception.ResourceNotFoundException;
import com.example.coinwallet.model.User;
import com.example.coinwallet.repository.UserRepository;
import com.example.coinwallet.service.UserService;
import com.example.coinwallet.utils.InMemoryCache;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final InMemoryCache<Long, UserWithTransactionsDTO> cache;

    private static final String USER_NOT_FOUND_MESSAGE = "User not found with id: ";
    private static final String EMAIL_ALREADY_EXISTS_MESSAGE = "Email already exists";
    private static final String ALL_USERS_CACHE_KEY = "all_users_with_transactions";

    @Override
    public List<UserDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList(); // Изменено здесь
    }

    @Override
    @Transactional
    public UserDTO saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(EMAIL_ALREADY_EXISTS_MESSAGE);
        }
        User savedUser = userRepository.save(user);
        UserWithTransactionsDTO userDto = convertToDto(savedUser);
        cacheUser(savedUser.getId(), userDto);
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
        UserWithTransactionsDTO userDto = convertToDto(updatedUser);
        cacheUser(id, userDto);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + id));
        userRepository.delete(user);
        removeUserFromCache(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserWithTransactionsDTO> getAllUsersWithTransactionsAndCategories() {
        // MODIFIED: Check cache for each user
        List<User> users = userRepository.findAllWithTransactionsAndCategories();
        return users.stream()
                .map(user -> {
                    UserWithTransactionsDTO cachedDto = cache.get(user.getId());
                    if (cachedDto != null) {
                        LOGGER.info("Returning cached user data for userId={}", user.getId());
                        return cachedDto;
                    }
                    LOGGER.info("Cache miss for userId={}, fetching from database", user.getId());
                    UserWithTransactionsDTO dto = convertToDto(user);
                    cache.put(user.getId(), dto);
                    LOGGER.info("Cached user data for userId={}", user.getId());
                    return dto;
                })
                .toList();
    }

    @Override
    public void clearCache() {
        cache.clear();
    }

    @Override
    public void cacheUser(Long userId, UserWithTransactionsDTO user) {
        cache.put(userId, user);
        LOGGER.info("User cached with userId={}", userId);
    }

    @Override
    public void removeUserFromCache(Long userId) {
        cache.remove(userId);
        LOGGER.info("User removed from cache with userId={}", userId);
    }

    @Override
    @Transactional
    public void updateUserCache(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + userId));
        UserWithTransactionsDTO userDto = convertToDto(user);
        cache.put(userId, userDto);
        LOGGER.info("User cache updated for id: {}", userId);
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