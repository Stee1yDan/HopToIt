package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.model.User;

import java.util.List;

public interface IUserService
{
    User registerUser(String username);
    User updateUser(UserDto user);
    User findUserByUsername(String username);
    User saveUser(User user);

    List<User> findAll();
    void deleteUser(Long id);

}
