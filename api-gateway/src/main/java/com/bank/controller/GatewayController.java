package com.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class GatewayController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody Map<String, Object> body) {

        String url = "http://auth-service:8081/auth/login";

        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                body,
                String.class
        );

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response.getBody());
    }
}
