package com.bank.controller;

import com.bank.util.JwtParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.data-service-url}")
    private String dataServiceUrl;

    /**
     * Extracts the JWT from the Authorization header and reads the role claim.
     * <p>
     * VULNERABILITY — Broken Function-Level Authorization (A01):
     * - The token's signature is NEVER verified (see JwtParser).
     * - Only the "role" claim value is checked — forge it to "admin".
     * - No re-validation against the database — a revoked/deleted user's token still works.
     * <p>
     * Attack:
     * 1. Get any valid JWT (or craft one with alg:none).
     * 2. Set "role":"admin" in the payload.
     * 3. Send with Authorization: Bearer <forged-token>.
     */
    private boolean isAdmin(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return false;
        String token = authHeader.substring(7);
        return "admin".equals(JwtParser.extractRole(token));
    }

    /**
     * List all users — admin only (but auth is bypassable).
     * VULNERABILITY: role check relies on unverified JWT claim.
     */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestHeader(value = "Authorization", required = false) String auth) {
        if (!isAdmin(auth)) {
            return ResponseEntity.status(403).body("Access denied");
        }
        return restTemplate.getForEntity(dataServiceUrl + "/data/users/all-internal", Object.class);
    }

    /**
     * Dump system environment variables.
     * VULNERABILITY — Sensitive Data Exposure (A02):
     * Exposes DB credentials, hostnames, API keys stored in env vars.
     * No meaningful auth — just the bypassable JWT role check.
     */
    @GetMapping("/env")
    public ResponseEntity<?> getEnv(@RequestHeader(value = "Authorization", required = false) String auth) {
        if (!isAdmin(auth)) {
            return ResponseEntity.status(403).body("Access denied");
        }
        return ResponseEntity.ok(System.getenv());
    }

    /**
     * Delete any user by ID.
     * VULNERABILITY — Broken Function-Level Auth + IDOR (A01):
     * Role check is bypassable; no secondary confirmation.
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String auth) {
        if (!isAdmin(auth)) {
            return ResponseEntity.status(403).body("Access denied");
        }
        restTemplate.delete(dataServiceUrl + "/data/users/" + id);
        return ResponseEntity.ok("User " + id + " deleted");
    }
}
