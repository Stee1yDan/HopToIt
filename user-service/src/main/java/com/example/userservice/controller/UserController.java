package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.model.User;
import com.example.userservice.interfaces.IUserService;
import com.example.userservice.util.DtoConverter;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController
{
    private final IUserService userService;
    @GetMapping("/getAll")
    public ResponseEntity<List<UserDto>> getAll()
    {
        return new ResponseEntity<>(userService.findAll().stream()
                .map(user -> DtoConverter.convertUser(user))
                .collect(Collectors.toList()), HttpStatus.OK);
    }
    
    @GetMapping("/register/{username}")
    @CircuitBreaker(name="default", fallbackMethod = "fallbackRegisterMethod")
    @Retry(name="default")
    public CompletableFuture<ResponseEntity<Void>> registerUser(@PathVariable("username") String username)
    {

        return CompletableFuture.supplyAsync(() ->
        {
            userService.registerUser(username);
            return new ResponseEntity<>(HttpStatus.CREATED);
        });
    }

    @GetMapping("/get/{username}")
    @CircuitBreaker(name="default", fallbackMethod = "fallbackGetMethod")
    @Retry(name="default")
    public CompletableFuture<ResponseEntity<UserDto>> getUser(@PathVariable("username") String username)
    {
        User user = userService.findUserByUsername(username);
        return CompletableFuture.supplyAsync(() ->{
            return new ResponseEntity<>(DtoConverter.convertUser(user),HttpStatus.OK);
        });

    }

    @PutMapping("/update")
    @CircuitBreaker(name="default", fallbackMethod = "fallbackUpdateMethod")
    @TimeLimiter(name="default")
    @Retry(name="default")
    public CompletableFuture<ResponseEntity<Void>> updateUser(@RequestBody UserDto user)
    {
        return CompletableFuture.supplyAsync(() ->
        {
            new ResponseEntity<>(DtoConverter.convertUser(userService.updateUser(user)), HttpStatus.OK);
            return null;
        });
    }

    @DeleteMapping("/delete/{username}")
    @CircuitBreaker(name="default", fallbackMethod = "fallbackDeleteMethod")
    @TimeLimiter(name="default")
    @Retry(name="default")
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

    public CompletableFuture<ResponseEntity<Void>> fallbackUpdateMethod(UserDto user, Throwable throwable)
    {
        System.out.println(throwable.toString());
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(null, HttpStatus.CONFLICT));
    }

    public CompletableFuture<ResponseEntity<UserDto>> fallbackGetMethod(String username, Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(null, HttpStatus.CONFLICT));
    }

    public CompletableFuture<ResponseEntity<Void>> deleteUser(String username, Throwable throwable)
    {
        return  CompletableFuture.supplyAsync(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }


}
