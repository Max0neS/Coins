package com.example.coinwallet.controller;

import com.example.coinwallet.dto.UserDTO;
import com.example.coinwallet.dto.UserWithTransactionsDTO;
import com.example.coinwallet.model.User;
import com.example.coinwallet.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<UserDTO> findAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        UserDTO savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<UserWithTransactionsDTO> findUserWithTransactions(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserWithTransactions(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/with-transactions")
    public ResponseEntity<List<UserWithTransactionsDTO>> getAllUsersWithTransactions() {
        List<UserWithTransactionsDTO> users =
                userService.getAllUsersWithTransactionsAndCategories();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/clear-cache")
    public ResponseEntity<Void> clearCache() {
        userService.clearCache();
        return ResponseEntity.ok().build();
    }
}