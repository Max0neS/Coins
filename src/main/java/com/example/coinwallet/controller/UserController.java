package com.example.coinwallet.controller;

import com.example.coinwallet.dto.UserDTO;
import com.example.coinwallet.dto.UserWithTransactionsDTO;
import com.example.coinwallet.model.User;
import com.example.coinwallet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Контроллер ПОЛЬЗОВАТЕЛЕЙ", description = "позволяет работать с пользователями")
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Operation(
            summary = "Вывод всех пользователей",
            description = "Позволяет просмотреть всех существующих пользователей"
    )
    @GetMapping("/get-all")
    public List<UserDTO> findAllUsers() {
        return userService.findAllUsers();
    }

    @Operation(
            summary = "Создание пользователя",
            description = "Позволяет создать нового пользователя"
    )
    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        UserDTO savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @Operation(
            summary = "Поиск пользователя по ID",
            description = "Позволяет найти пользователя по ID"
    )
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(
            summary = "Вывод всех транзакций пользователя",
            description = "Позволяет просмотреть транзакции пользователя"
    )
    @GetMapping("/get-user-transactions-by-id/{id}")
    public ResponseEntity<UserWithTransactionsDTO> findUserWithTransactions(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserWithTransactions(id));
    }

    @Operation(
            summary = "Обновление пользователя",
            description = "Позволяет обновить информацию о пользователе"
    )
    @PutMapping("/update-by-id/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @Operation(
            summary = "Удаление пользователя",
            description = "Позволяет удалить пользователя по ID"
    )
    @DeleteMapping("/del-by-id/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Вывод всех пользователей с транзакциями",
            description = "Позволяет просмотреть полную информацию о всех пользователях"
    )
    @GetMapping("/show-users-and-transactions")
    public ResponseEntity<List<UserWithTransactionsDTO>> getAllUsersWithTransactions() {
        List<UserWithTransactionsDTO> users =
                userService.getAllUsersWithTransactionsAndCategories();
        return ResponseEntity.ok(users);
    }
}