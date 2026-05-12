package com.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class SsrfController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Fetch any URL server-side and return the response.
     * <p>
     * VULNERABILITY — Server-Side Request Forgery (SSRF) (A10):
     * The url parameter is used without any validation or allowlist.
     * <p>
     * Attack scenarios:
     * - Cloud metadata: GET /api/fetch?url=http://169.254.169.254/latest/meta-data/
     * - Internal service: GET /api/fetch?url=http://data-service:8082/actuator/env
     * - Internal service bypass: GET /api/fetch?url=http://data-service:8082/data/users/id/1
     * - Port scanning: GET /api/fetch?url=http://mariadb:3306
     */
    @GetMapping("/fetch")
    public ResponseEntity<String> fetch(@RequestParam String url) {
        try {
            String body = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching URL: " + e.getMessage());
        }
    }
}
