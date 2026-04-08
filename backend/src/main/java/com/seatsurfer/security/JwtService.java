package com.seatsurfer.security;

import com.seatsurfer.config.AppProperties;
import com.seatsurfer.entity.AdminUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final Key signingKey;
    private final AppProperties appProperties;

    public JwtService(AppProperties appProperties) {
        this.appProperties = appProperties;
        this.signingKey = Keys.hmacShaKeyFor(appProperties.getAuth().getJwtSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(AdminUser adminUser) {
        Date now = new Date();
        Date expiry = Date.from(expiresAt().atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
            .subject(adminUser.getUsername())
            .claim("role", adminUser.getRole().name())
            .claim("fullName", adminUser.getFullName())
            .issuedAt(now)
            .expiration(expiry)
            .signWith(signingKey)
            .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isValid(String token, SeatSurferAdminPrincipal principal) {
        Claims claims = parseClaims(token);
        return principal.getUsername().equalsIgnoreCase(claims.getSubject())
            && claims.getExpiration().after(new Date());
    }

    public LocalDateTime expiresAt() {
        return LocalDateTime.now().plusMinutes(appProperties.getAuth().getJwtExpirationMinutes());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
            .verifyWith((javax.crypto.SecretKey) signingKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
