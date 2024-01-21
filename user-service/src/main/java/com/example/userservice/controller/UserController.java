package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController
{
    private final UserService userService;
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAll()
    {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/register/{username}")
    @CircuitBreaker(name="user-controller", fallbackMethod = "fallbackMethod")
    @Retry(name="user-controller")
    public CompletableFuture<ResponseEntity<User>> registerUser(@PathVariable("username") String username)
    {
        return CompletableFuture.supplyAsync(() ->
                new ResponseEntity<>(userService.registerUser(username), HttpStatus.CREATED));
    }

    @PutMapping("/update")
    @CircuitBreaker(name="user-controller", fallbackMethod = "fallbackUpdateUserMethod")
    @TimeLimiter(name="user-controller")
    @Retry(name="user-controller")
    public CompletableFuture<ResponseEntity<User>> updateUser(@RequestBody User user)
    {
        User currentUser = userService.findUserByUsername(user.getUsername()); // TODO: Check if user exist
        user.setId(currentUser.getId());
        return CompletableFuture.supplyAsync(() ->
            new ResponseEntity<>(userService.updateUser(user), HttpStatus.CREATED));
    }

    @DeleteMapping("/delete/{username}")
    @CircuitBreaker(name="user-controller", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name="user-controller")
    @Retry(name="user-controller")
    public CompletableFuture<ResponseEntity<Object>> deleteUser(@PathVariable("username") String username)   //TODO: Block user in auth-service
    {
        userService.deleteUser(userService.findUserByUsername(username).getId());
        return CompletableFuture.supplyAsync(() ->
            new ResponseEntity<>(HttpStatus.NO_CONTENT));//TODO: Verify deletion by email
    }

    public CompletableFuture<ResponseEntity<User>> fallbackMethod(String username, Throwable exception)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(null, HttpStatus.CONFLICT));
    }

    public CompletableFuture<ResponseEntity<User>> fallbackUpdateUserMethod(User user, Throwable exception)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(null, HttpStatus.CONFLICT));
    }


}
