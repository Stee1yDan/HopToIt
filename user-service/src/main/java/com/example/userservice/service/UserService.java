package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;

    public User saveUser(User user)
    {
        return userRepository.save(user);
    }

    public List<User> findAll()
    {
        return userRepository.findAll();
    }
}
