package com.example.coinwallet.controller;

import com.example.coinwallet.model.User;
import com.example.coinwallet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor

public class UserController {
    private UserService service;

    @GetMapping
    public List<User> findAllUser(){
        return service.findAllUser();
    }

    @PostMapping("save_student")
    public String saveUser(@RequestBody User user){
        service.saveUser(user);
        return "successfully save";
    }

    @GetMapping("/search")
    public User searchUser(@RequestParam(required = false) String name){
        return service.searchUser(name);
    }

    @GetMapping("/{name}")
    public User findByName(@PathVariable String name){
        return service.findByName(name);
    }

    @PutMapping("update_student")
    public User updateUser(@RequestBody User user){
        return service.updateUser(user);
    }

    @DeleteMapping("delete_student/{name}")
    public void deleteUser(@PathVariable String name){
        service.deleteUser(name);
    }

}
