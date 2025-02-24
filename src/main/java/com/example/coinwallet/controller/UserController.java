package com.example.coinwallet.controller;

import com.example.coinwallet.model.User;
import com.example.coinwallet.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Контроллер для управления пользователями.
 *
 * <p>Подавляет предупреждения</p>
 */
@SuppressWarnings("checkstyle:Indentation")
@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor

public class UserController {
        /**
         * Переменная сервиса для управления пользователями.
         */
        private UserService service;

        /**
         * Получает список всех пользователей.
         *
         * @return список пользователей
         */
        @GetMapping
        public List<User> findAllUser() {
            return service.findAllUser();
        }

        /**
         * Сохраняет нового пользователя.
         *
         * @param user объект пользователя, который нужно сохранить
         * @return сообщение о результате операции
         */
        @PostMapping("save_student")
        public String saveUser(@RequestBody final User user) {
            service.saveUser(user);
            return "successfully save";
        }

        /**
         * Ищет пользователя по имени.
         *
         * @param name имя пользователя
         * @return найденный пользователь или null, если не найден
         */
        @GetMapping("/search")
        public User searchUser(@RequestParam(required = false) final String name) {
            return service.searchUser(name);
        }

        /**
         * Находит пользователя по имени.
         *
         * @param name имя пользователя
         * @return найденный пользователь
         */
        @GetMapping("/{name}")
        public User findByName(@PathVariable final String name) {
            return service.findByName(name);
        }

        /**
         * Обновляет данные существующего пользователя.
         *
         * @param user объект пользователя с обновленными данными
         * @return обновленный пользователь
         */
        @PutMapping("update_student")
        public User updateUser(@RequestBody final User user) {
            return service.updateUser(user);
        }

        /**
         * Удаляет пользователя по имени.
         *
         * @param name имя пользователя, которого нужно удалить
         */
        @DeleteMapping("delete_student/{name}")
        public void deleteUser(@PathVariable final String name) {
            service.deleteUser(name);
        }

}
