package com.mracover.telegram_bot.controller;

import com.mracover.telegram_bot.model.User;
import com.mracover.telegram_bot.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/telegramId/{id}")
    public ResponseEntity<User> getUserByTelegramId(@PathVariable("id") long id) {
        User user = userService.findUserByTelegramId(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/telegramId/{id}")
    public ResponseEntity<Void> deleteUserByTelegramId(@PathVariable long id) {
        userService.deleteUserByTelegramId(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
