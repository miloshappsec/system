package com.bank.controller;

import com.bank.model.User;
import com.bank.service.KafkaProducerService;
import com.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @GetMapping("/id/{id}")
    public User getById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/username/{username}")
    public User getByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }


    @PostMapping("/all")
    public List<User> get(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        User user = userService.findUserByUsername(username);
        if (user != null && user.getPassword().equals(password) && user.getRole().equals("admin")) {
            return userService.getAllUsers();
        }
        return null;
    }

    @PostMapping("create")
    public User create(@RequestBody User user) {
        userService.createUser(user);
        kafkaProducerService.sendUserCreatedEvent(user.getUsername());
        return user;
    }
}