package com.bank.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * VULNERABILITY — CORS Misconfiguration (A05):
     * - Allows any origin (wildcard)
     * - Allows credentials (cookies, Authorization headers) from any origin
     * - Allows all HTTP methods including DELETE
     * - Reflects the request Origin header rather than using a fixed allowlist
     * <p>
     * This allows a malicious site to make credentialed cross-origin requests
     * to the API on behalf of an authenticated user (CSRF via CORS).
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}