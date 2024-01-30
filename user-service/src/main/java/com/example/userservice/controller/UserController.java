package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.service.IUserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController
{
    private final IUserService userService;
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAll()
    {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/register/{username}")
    @CircuitBreaker(name="user-controller", fallbackMethod = "fallbackRegisterMethod")
    @Retry(name="user-controller")
    public CompletableFuture<ResponseEntity<Void>> registerUser(@PathVariable("username") String username)
    {

        return CompletableFuture.supplyAsync(() ->
        {
            userService.registerUser(username);
            return new ResponseEntity<>(HttpStatus.CREATED);
        });
    }

    @GetMapping("/get/{username}")
    @CircuitBreaker(name="user-controller", fallbackMethod = "fallbackGetMethod")
    @Retry(name="user-controller")
    public CompletableFuture<ResponseEntity<User>> getUser(@PathVariable("username") String username)
    {

        return CompletableFuture.supplyAsync(() ->
                new ResponseEntity<>(userService.findUserByUsername(username),HttpStatus.CREATED));
    }

    @PutMapping("/update")
    @CircuitBreaker(name="user-controller", fallbackMethod = "fallbackUpdateMethod")
    @TimeLimiter(name="user-controller")
    @Retry(name="user-controller")
    public CompletableFuture<ResponseEntity<User>> updateUser(@RequestBody User user)
    {
        return CompletableFuture.supplyAsync(() ->
        {
            User currentUser = userService.findUserByUsername(user.getUsername());
            user.setId(currentUser.getId());
            return new ResponseEntity<>(userService.updateUser(user), HttpStatus.CREATED);
        });
    }

    @DeleteMapping("/delete/{username}")
    @CircuitBreaker(name="user-controller", fallbackMethod = "fallbackDeleteMethod")
    @TimeLimiter(name="user-controller")
    @Retry(name="user-controller")
    public CompletableFuture<ResponseEntity<Void>> deleteUser(@PathVariable("username") String username)   //TODO: Block user in auth-service
    {

        return CompletableFuture.supplyAsync(() ->
        {
            userService.deleteUser(userService.findUserByUsername(username).getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);//TODO: Verify deletion by email
        });
    }

    public CompletableFuture<ResponseEntity<Void>> fallbackRegisterMethod(String username, Throwable throwable)
    {
        //TODO: Send an email to admin
        return CompletableFuture.supplyAsync(() ->
        {
            log.info("WARNING! Couldn't register the user: ", username);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        });
    }

    public CompletableFuture<ResponseEntity<User>> fallbackUpdateMethod(User user, Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(user, HttpStatus.CONFLICT));
    }

    public CompletableFuture<ResponseEntity<User>> fallbackGetMethod(String username, Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(null, HttpStatus.CONFLICT));
    }

    public CompletableFuture<ResponseEntity<Void>> deleteUser(String username, Throwable throwable)
    {
        return  CompletableFuture.supplyAsync(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }


}
