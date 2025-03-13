package com.example.coinwallet.repository;

import com.example.coinwallet.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для управления пользователями в памяти.
 * Позволяет выполнять операции CRUD (создание, чтение, обновление, удаление)
 * над пользователями, хранящимися в списке.
 */

@Repository
public class InMemoryUserDAO {

    /**
       * Список пользователей.
       */
    private final List<User> usersList = new ArrayList<>();

    /**
       * Находит всех пользователей.
       *
       * @return Список всех пользователей.
       */
    public List<User> findAllUser() {
      return usersList;
    }

    /**
       * Сохраняет нового пользователя в репозитории.
       *
       * @param newUser Пользователь, который нужно сохранить.
       * @return Сохраненный пользователь.
       */
    public User saveUser(final User newUser) {
        for (User user : usersList) {
            if (user.getId().equals(newUser.getId())) {
                throw new IllegalArgumentException("Пользователь с ID " + newUser.getId() + " уже существует.");
            }
        }

        usersList.add(newUser);
        return newUser;
    }

    /**
       * Находит пользователя по имени.
       *
       * @param name Имя пользователя для поиска.
       * @return Пользователь с указанным именем или null
       */

    public User findByName(final String name) {
        return usersList.stream()
                .filter(element -> element.getName()
                        .equals(name)).findFirst()
                .orElse(null);
    }

    public List<User> findAllByName(final String name) {
        return usersList.stream()
                .filter(element -> element.getName().equals(name))
                .toList(); // Используем Stream.toList()
    }

    public User findById(final Integer id) {
        return usersList.stream()
                .filter(element -> element.getId().equals(id)) // Фильтруем по id
                .findFirst() // Находим первый элемент, который соответствует фильтру
                .orElse(null); // Если не найдено, возвращаем null
    }

    /**
       * Обновляет информацию о пользователе.
       *
       * @param user Пользователь с обновленными данными.
       * @return Обновленный пользователь или null, если пользователь не найден.
       */
    public User updateUser(final User user) {
        // Ищем индекс пользователя по его ID
        var userIndex = IntStream.range(0, usersList.size())
                .filter(index -> usersList.get(index).getId().equals(user.getId())) // Сравниваем по ID
                .findFirst()
                .orElse(-1); // Если не найден, возвращаем -1

        if (userIndex > -1) {
            usersList.set(userIndex, user);
            return user; // Возвращаем обновленного пользователя
        }

        return null; // Если пользователь не найден, возвращаем null
    }

    /**
       * Удаляет пользователя по id.
       *
       * @param id id пользователя, которого нужно удалить.
       */
    public void deleteUser(final Integer id) {
      var user = findById(id);
      if (user != null) {
        usersList.remove(user);
      }
    }
}
