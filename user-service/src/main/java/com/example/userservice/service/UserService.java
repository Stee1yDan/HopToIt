package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.interfaces.IUserService;
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
    public User updateUser(UserDto user)
    {
        User currentUser = userRepository.findUserByUsername(user.getUsername());
        currentUser.setDescription((user.getDescription()));
        return userRepository.save(currentUser);
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
