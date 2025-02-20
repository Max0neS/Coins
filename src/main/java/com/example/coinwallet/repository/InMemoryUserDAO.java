package com.example.coinwallet.repository;

import com.example.coinwallet.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Repository

public class InMemoryUserDAO {

    private final List<User> USERS = new ArrayList<>();

    public List<User> findAllUser(){
        return USERS;
    }

    public User saveUser(User newUser){
        USERS.add(newUser);
        return newUser;
    }

    public User findByName(String name){
        return USERS.stream()
                .filter(element -> element.getName()
                        .equals(name)).findFirst()
                .orElse(null);
    }

    public User updateUser(User user){
        var userIndex = IntStream.range(0, USERS.size())
                .filter(index -> USERS.get(index).getName().equals(user.getName()))
                .findFirst()
                .orElse(-1);
        if(userIndex > - 1){
            USERS.set(userIndex, user);
            return user;
        }
        return null;
    }

    public void deleteUser(String name){
        var user = findByName(name);
        if (user != null){
            USERS.remove(user);
        }
    }

}
