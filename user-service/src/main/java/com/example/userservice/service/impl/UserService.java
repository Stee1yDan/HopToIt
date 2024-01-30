package com.example.userservice.service.impl;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService
{
    private final UserRepository userRepository;

    @Override
    public User registerUser(String username)
    {
        return userRepository.save(User.builder().username(username).build());
    }

    @Override
    public User updateUser(User user)
    {
        return userRepository.save(user);
    }
    @Override
    public User findUserByUsername(String username)
    {
        return userRepository.findUserByUsername(username);
    }
    @Override
    public User saveUser(User user)
    {
        return userRepository.save(user);
    }
    @Override
    public List<User> findAll()
    {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id)
    {
        userRepository.deleteById(id);
    }
}
