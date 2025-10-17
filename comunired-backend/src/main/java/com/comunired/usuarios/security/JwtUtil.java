package com.comunired.usuarios.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    // En producción, usa una variable de entorno segura por ejemplo JWT_SECRET
    private final String secret = System.getenv().getOrDefault("JWT_SECRET", "CAMBIA_POR_UNA_LLAVE_MUY_LARGA_Y_SECRETA_QUE_SUPERE_32_BYTES");
    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());
    private final long expirationMs = 7 * 24 * 60 * 60 * 1000L; // 7 días

    public String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}