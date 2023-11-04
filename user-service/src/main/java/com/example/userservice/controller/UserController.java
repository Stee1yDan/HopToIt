package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAll()
    {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/register/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@PathVariable("username") String username)
    {
        userService.registerUser(username);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user)
    {
        User currentUser = userService.findUserByUsername(user.getUsername()); // TODO: Check if user exist
        user.setId(currentUser.getId());
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("username") String username)   //TODO: Block user in auth-service
    {
        userService.deleteUser(userService.findUserByUsername(username).getId());//TODO: Verify deletion by email
    }

}
