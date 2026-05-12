package com.bank.controller;

import com.bank.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/create")
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.data-service-url}")
    private String dataServiceUrl;

    // No authentication required — anyone can create accounts
    // No input validation — mass assignment (caller can set role=admin, balance=999999)
    @PostMapping("/user")
    public String createUser(@RequestBody User user) {

        User created = restTemplate.postForObject(
                dataServiceUrl + "/data/users/create",
                user,
                User.class
        );

        return created != null ? "OK" : "FAIL";
    }
}