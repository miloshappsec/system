package com.bank.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Base64;
import java.util.Map;

/**
 * Intentionally insecure JWT parser.
 * <p>
 * VULNERABILITIES:
 * - Signature is NEVER verified — the third segment of the token is completely ignored.
 * - Algorithm is NEVER checked — alg:none tokens are accepted.
 * - Any attacker can:
 * 1. Decode the payload (base64url)
 * 2. Modify claims (e.g. "role": "admin")
 * 3. Re-encode and submit with or without a signature
 * and the server will trust the modified claims.
 * <p>
 * Attack (alg:none):
 * header = base64url({"alg":"none","typ":"JWT"})
 * payload = base64url({"username":"alice","role":"admin"})
 * token = header + "." + payload + "."   (empty signature)
 * <p>
 * Attack (weak secret — crack with hashcat):
 * hashcat -a 0 -m 16500 <token> /usr/share/wordlists/rockyou.txt
 */
public class JwtParser {

    private static final Gson gson = new Gson();

    public static Map<String, Object> extractClaims(String token) {
        if (token == null || !token.contains(".")) {
            return null;
        }
        String[] parts = token.split("\\.");
        if (parts.length < 2) {
            return null;
        }
        try {
            // Decode only the payload — signature (parts[2]) is never checked
            byte[] payloadBytes = Base64.getUrlDecoder().decode(padBase64(parts[1]));
            String payload = new String(payloadBytes);
            return gson.fromJson(payload, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (Exception e) {
            return null;
        }
    }

    public static String extractRole(String token) {
        Map<String, Object> claims = extractClaims(token);
        if (claims == null) return null;
        Object role = claims.get("role");
        return role != null ? role.toString() : null;
    }

    public static String extractUsername(String token) {
        Map<String, Object> claims = extractClaims(token);
        if (claims == null) return null;
        Object username = claims.get("username");
        return username != null ? username.toString() : null;
    }

    private static String padBase64(String base64) {
        int mod = base64.length() % 4;
        return mod == 0 ? base64 : base64 + "=".repeat(4 - mod);
    }
}
