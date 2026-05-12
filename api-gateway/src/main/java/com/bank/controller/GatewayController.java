package com.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class GatewayController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.auth-service-url}")
    private String authServiceUrl;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> body) {
        ResponseEntity<Object> response = restTemplate.postForEntity(authServiceUrl + "/auth/login", body, Object.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // Register a new user — proxied to auth-service which calls data-service
    // No auth required; mass assignment possible (set role=admin in the body)
    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody Map<String, Object> body) {
        ResponseEntity<String> response = restTemplate.postForEntity(authServiceUrl + "/create/user", body, String.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @GetMapping
    public Map<String, String> endpoints() {
        return Map.of(
                "login", "POST /api/auth/login",
                "register", "POST /api/auth/register",
                "challenges", "GET  /api/challenges",
                "users", "GET  /data/users/id/{id}",
                "ssrf", "GET  /api/fetch?url=<any-url>",
                "admin", "GET  /api/admin/users (requires Authorization: Bearer <token>)"
        );
    }
}
