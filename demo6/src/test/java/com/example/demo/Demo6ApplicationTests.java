package com.example.demo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class JwtTest {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Test
    void generateAndValidateJwt() {
        String token = Jwts.builder()
                .setSubject("testuser")
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        String subject = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        assertEquals("testuser", subject);
    }
}
