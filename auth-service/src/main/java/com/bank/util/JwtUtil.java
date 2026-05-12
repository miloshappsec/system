package com.bank.util;

import com.bank.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    // Intentionally weak, hardcoded 6-byte secret — far below the 256-bit HS256 minimum.
    // jjwt 0.9.1 does NOT enforce minimum key size, so this works (unlike 0.11.x).
    // Crackable with: hashcat -a 0 -m 16500 <token> rockyou.txt
    public static final String SECRET = "secret";

    public static String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        claims.put("email", user.getEmail());
        claims.put("balance", user.getBalance());

        // Raw byte[] key — 6 bytes = 48 bits, grossly undersized for HS256.
        // No expiration set — tokens are valid indefinitely.
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                .compact();
    }
}
