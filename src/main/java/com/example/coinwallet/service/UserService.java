package com.example.coinwallet.service;

import com.example.coinwallet.model.User;

import java.util.List;

public interface UserService {
    List<User> findAllUser();

    User saveUser(User newUser);

    User searchUser(String name);

    User findByName(String name);

    User updateUser(User user);

    void deleteUser(String name);
}
