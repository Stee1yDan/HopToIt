package com.example.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "user-service", url = "http://localhost:4358")
public interface UserClient
{
    @GetMapping("/register/{username}")
    void registerUser(@PathVariable("username") String username);
}
