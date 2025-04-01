package com.example.coinwallet.service.impl;

import com.example.coinwallet.model.User;
import com.example.coinwallet.repository.UserRepository;
import com.example.coinwallet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public List<User> findAllUser() {
        return repository.findAll();
    }

    @Override
    public User saveUser(User newUser) {
        return repository.save(newUser);
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
        return repository.findAllByName(name);
    }

    @Override
    public Optional<User> findById(final Long id) {
        return repository.findById(id);
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
