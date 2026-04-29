package com.bank.controller;


import com.bank.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/user")
    public String login(@RequestBody User user) {


        Boolean created = restTemplate.postForObject(
                "http://data-service/data/users/", user,
                Boolean.class
        );

        if (created) {
            return "OK";
        }

        return "FAIL";
    }

}