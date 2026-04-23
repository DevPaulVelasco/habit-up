package org.example.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    // Generar el token
    public String generateToken(Authentication authentication) {
        String email = authentication.getName();

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    // Generación por email directo para que sea más fácil en este proyecto sin UserDetailsService complejo
    public String generateToken(String email) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Obtener email (subject) del token
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Validar token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            System.out.println("Invalid JWT Token: " + ex.getMessage());
        }
        return false;
    }
}
