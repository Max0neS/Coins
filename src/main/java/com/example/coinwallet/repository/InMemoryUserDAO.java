package com.example.coinwallet.repository;

import com.example.coinwallet.model.User;
import java.util.ArrayList;
import java.util.List;
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
  private final List<User> USERS = new ArrayList<>();

  /**
     * Находит всех пользователей.
     *
     * @return Список всех пользователей.
     */
  public List<User> findAllUser() {
    return USERS;
  }

  /**
     * Сохраняет нового пользователя в репозитории.
     *
     * @param newUser Пользователь, который нужно сохранить.
     * @return Сохраненный пользователь.
     */
  public User saveUser(final User newUser) {
    USERS.add(newUser);
    return newUser;
  }

  /**
     * Находит пользователя по имени.
     *
     * @param name Имя пользователя для поиска.
     * @return Пользователь с указанным именем или null
     */
  public User findByName(final String name) {
    return USERS.stream()
                .filter(element -> element.getName()
                        .equals(name)).findFirst()
                .orElse(null);
  }

  /**
     * Обновляет информацию о пользователе.
     *
     * @param user Пользователь с обновленными данными.
     * @return Обновленный пользователь или null, если пользователь не найден.
     */
  public User updateUser(final User user) {
    var userIndex = IntStream.range(0, USERS.size())
                .filter(index -> USERS.get(index)
                        .getName()
                        .equals(user.getName()))
                .findFirst()
                .orElse(-1);
    if (userIndex > -1) {
      USERS.set(userIndex, user);
      return user;
    }
    return null;
  }

  /**
     * Удаляет пользователя по имени.
     *
     * @param name Имя пользователя, которого нужно удалить.
     */
  public void deleteUser(final String name) {
    var user = findByName(name);
    if (user != null) {
      USERS.remove(user);
    }
  }

}
