package com.example.coinwallet.service.impl;

import com.example.coinwallet.model.User;
import com.example.coinwallet.repository.InMemoryUserDAO;
import com.example.coinwallet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class InMemoryUserService implements UserService {

    private final InMemoryUserDAO repository;

    @Override
    public List<User> findAllUser() {
        return repository.findAllUser();
    }

    @Override
    public User saveUser(User newUser) {
        return repository.saveUser(newUser);
    }

    @Override
    public User searchUser(String name) {
        if (name != null) {
            return repository.findByName(name);
        } else {
            return null;
        }
    }

    @Override
    public User findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<User> findAllByName(String name) {
        if (name != null) {
            return repository.findAllByName(name);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public User findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public User updateUser(User user) {
        return repository.updateUser(user);
    }

    @Override
    public void deleteUser(Integer id) {
        repository.deleteUser(id);
    }
}
