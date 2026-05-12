package com.bank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/challenges")
public class ChallengesController {

    @GetMapping
    public List<Map<String, Object>> getChallenges() {
        return List.of(

                Map.of(
                        "id", 1,
                        "category", "A01 - Broken Access Control",
                        "title", "IDOR — Read Another User's Data",
                        "difficulty", "Easy",
                        "endpoint", "GET /data/users/id/{id}",
                        "hint", "The endpoint accepts any numeric ID. Try changing the ID to access another user's account. No token is required.",
                        "goal", "Read the full profile (including password) of user ID 2"
                ),

                Map.of(
                        "id", 2,
                        "category", "A01 - Broken Access Control",
                        "title", "IDOR — Read Another User's Transactions",
                        "difficulty", "Easy",
                        "endpoint", "GET /data/transactions/{userId}",
                        "hint", "Transaction history is returned for any userId with no authentication. Enumerate user IDs.",
                        "goal", "Retrieve the complete transaction history of a user you did not register"
                ),

                Map.of(
                        "id", 3,
                        "category", "A01 - Broken Access Control",
                        "title", "Broken Function-Level Auth — Admin Endpoint",
                        "difficulty", "Medium",
                        "endpoint", "GET /api/admin/users",
                        "hint", "The admin check reads the 'role' claim from your JWT without verifying the signature. Decode your token, change role to admin, re-encode, and send it.",
                        "goal", "Access /api/admin/users as a non-admin user"
                ),

                Map.of(
                        "id", 4,
                        "category", "A02 - Cryptographic Failures",
                        "title", "Crack the JWT Secret",
                        "difficulty", "Medium",
                        "endpoint", "POST /api/auth/login",
                        "hint", "Log in to receive a JWT. The HMAC-SHA256 secret is a common English word. Use hashcat: hashcat -a 0 -m 16500 <token> rockyou.txt",
                        "goal", "Recover the JWT signing secret and forge a token with role:admin"
                ),

                Map.of(
                        "id", 5,
                        "category", "A02 - Cryptographic Failures",
                        "title", "alg:none JWT Bypass",
                        "difficulty", "Hard",
                        "endpoint", "GET /api/admin/env",
                        "hint", "The gateway's JWT parser never checks the algorithm or signature. Craft a token with alg:none, set role:admin in the payload, and omit the signature segment.",
                        "goal", "Access /api/admin/env using a self-crafted alg:none token"
                ),

                Map.of(
                        "id", 6,
                        "category", "A03 - Injection",
                        "title", "SQL Injection — User Lookup",
                        "difficulty", "Medium",
                        "endpoint", "GET /data/users/username/{username}",
                        "hint", "The username is concatenated directly into a raw SQL query. Try a classic payload: admin' OR '1'='1",
                        "goal", "Dump all users from the database using SQL injection"
                ),

                Map.of(
                        "id", 7,
                        "category", "A03 - Injection",
                        "title", "SQL Injection — Transfer Endpoint",
                        "difficulty", "Hard",
                        "endpoint", "POST /data/transactions/transfer",
                        "hint", "The fromId and toId fields are concatenated into raw UPDATE queries. Inject SQL to manipulate account balances beyond the intended transfer.",
                        "goal", "Use SQL injection in the transfer endpoint to set your balance to 1000000"
                ),

                Map.of(
                        "id", 8,
                        "category", "A04 - Insecure Design",
                        "title", "Negative Transfer — Self-Enrichment",
                        "difficulty", "Easy",
                        "endpoint", "POST /data/transactions/transfer",
                        "hint", "There is no validation on the amount field. Send a negative amount to increase your own balance.",
                        "goal", "Increase your account balance without admin access by using a negative transfer amount"
                ),

                Map.of(
                        "id", 9,
                        "category", "A04 - Insecure Design",
                        "title", "Mass Assignment — Privilege Escalation",
                        "difficulty", "Easy",
                        "endpoint", "PUT /data/users/{id}",
                        "hint", "The update endpoint accepts all User fields including 'role'. Send { \"role\": \"admin\" } to escalate your own account.",
                        "goal", "Change your account role to 'admin' using the profile update endpoint"
                ),

                Map.of(
                        "id", 10,
                        "category", "A05 - Security Misconfiguration",
                        "title", "Exposed Actuator — Credential Leak",
                        "difficulty", "Easy",
                        "endpoint", "GET http://localhost:8082/actuator/env",
                        "hint", "Spring Boot Actuator is fully exposed with show-values=ALWAYS. The /actuator/env endpoint reveals all configuration properties including database credentials.",
                        "goal", "Extract the database username and password from the actuator endpoint"
                ),

                Map.of(
                        "id", 11,
                        "category", "A05 - Security Misconfiguration",
                        "title", "Gateway Bypass — Direct Data-Service Access",
                        "difficulty", "Easy",
                        "endpoint", "http://localhost:8082/data/users/id/1",
                        "hint", "The data-service port (8082) is exposed directly. No gateway rules or auth checks apply when you call it directly.",
                        "goal", "Access admin user data by bypassing the API gateway entirely"
                ),

                Map.of(
                        "id", 12,
                        "category", "A07 - Auth / Session Failures",
                        "title", "Brute Force Login — No Rate Limiting",
                        "difficulty", "Easy",
                        "endpoint", "POST /api/auth/login",
                        "hint", "There is no rate limiting, account lockout, or CAPTCHA on the login endpoint. Use a tool like Hydra or Burp Intruder with a wordlist.",
                        "goal", "Brute-force the password for user 'alice'"
                ),

                Map.of(
                        "id", 13,
                        "category", "A10 - SSRF",
                        "title", "Server-Side Request Forgery",
                        "difficulty", "Medium",
                        "endpoint", "GET /api/fetch?url=",
                        "hint", "The gateway fetches any URL provided in the query string server-side. Try: /api/fetch?url=http://data-service:8082/actuator/env to hit internal services.",
                        "goal", "Use SSRF to read the data-service actuator env endpoint from inside the network"
                ),

                Map.of(
                        "id", 14,
                        "category", "Path Traversal / LFI",
                        "title", "Local File Inclusion via File Download",
                        "difficulty", "Medium",
                        "endpoint", "GET /data/files/{filename}",
                        "hint", "The filename is resolved directly without boundary checks. Try: GET /data/files/..%2F..%2Fetc%2Fpasswd",
                        "goal", "Read /etc/passwd through the file download endpoint"
                ),

                Map.of(
                        "id", 15,
                        "category", "A02 - Cryptographic Failures",
                        "title", "Plaintext Passwords in Database",
                        "difficulty", "Easy",
                        "endpoint", "GET /data/users/id/{id}",
                        "hint", "Passwords are stored and returned in plaintext. Retrieve any user object — the password field is present in the response.",
                        "goal", "Extract admin's plaintext password from the API response"
                )
        );
    }
}
