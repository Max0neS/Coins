package com.example.coinwallet.service.Impl;

import com.example.coinwallet.model.User;
import com.example.coinwallet.repository.InMemoryUserDAO;
import com.example.coinwallet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InMemoryUserService implements UserService{

    private final InMemoryUserDAO repository;

    @Override
    public List<User> findAllUser(){
        return repository.findAllUser();
    }

    @Override
    public User saveUser(User newUser) {
        return repository.saveUser(newUser);
    }

    @Override
    public User findByName(String name){
        return repository.findByName(name);
    }

    @Override
    public User updateUser(User user){
        return repository.updateUser(user);
    }

    @Override
    public void deleteUser(String name){
        repository.deleteUser(name);
    }
}
