package com.bank.controller;

import com.bank.model.User;
import com.bank.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.data-service-url}")
    private String dataServiceUrl;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        String username = body.get("username");
        String password = body.get("password");

        // No input validation — SQL injection travels through to data-service
        // URL path concatenation allows path manipulation
        User user;
        try {
            user = restTemplate.getForObject(
                    dataServiceUrl + "/data/users/username/" + username,
                    User.class
            );
        } catch (RestClientException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // Plaintext password comparison — no hashing
        if (user != null && user.getPassword().equals(password)) {
            String token = JwtUtil.generateToken(user);

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("role", user.getRole());
            userMap.put("email", user.getEmail());
            userMap.put("balance", user.getBalance());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", userMap);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }
}