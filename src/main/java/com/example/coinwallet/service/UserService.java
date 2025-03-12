package com.example.coinwallet.service;

import com.example.coinwallet.model.User;

import java.util.List;

public interface UserService {
    List<User> findAllUser();

    User saveUser(User newUser);

    User searchUser(String name);

    User findByName(String name);

    List<User> findAllByName(final String name);

    User findById(Integer id);

    User updateUser(User user);

    void deleteUser(Integer id);
}
