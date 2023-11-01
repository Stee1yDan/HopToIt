package com.example.userservice.service;

import com.example.userservice.model.User;

import java.util.List;

public interface UserService
{
    User registerUser(String username);
    User updateUser(User user);
    User findUserByUsername(String username);
    User saveUser(User user);

    List<User> findAll();
    void deleteUser(Long id);

}
