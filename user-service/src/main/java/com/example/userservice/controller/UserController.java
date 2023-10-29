package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAll()
    {
        return new ResponseEntity<>(userService.findAll(), HttpStatusCode.valueOf(200));
    }

}
