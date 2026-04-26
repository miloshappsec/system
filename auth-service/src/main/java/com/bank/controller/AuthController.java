package com.bank.controller;



import com.bank.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> body) {

        String username = body.get("username");
        String password = body.get("password");

        // calls data-service
        User user = restTemplate.getForObject(
                "http://data-service/data/users/" + username,
                User.class
        );

        if (user != null && user.getPassword().equals(password)) {
            return "OK";
        }

        return "FAIL";
    }
}