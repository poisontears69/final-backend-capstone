package com.healthconnect.finalbackendcapstone.security;

import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.util.EnvironmentUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final EnvironmentUtil environmentUtil;
    private final long jwtExpirationMs = 86400000; // 24 hours

    public JwtUtil(EnvironmentUtil environmentUtil) {
        this.environmentUtil = environmentUtil;
    }

    /**
     * Generate JWT token for a user
     *
     * @param user the user to generate token for
     * @return the JWT token
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("email", user.getEmail());
        claims.put("id", user.getId());
        
        return createToken(claims, user.getEmail());
    }

    /**
     * Create a JWT token
     *
     * @param claims  the claims to include in the token
     * @param subject the subject of the token (usually the user email)
     * @return the JWT token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Get the signing key for JWT
     *
     * @return the signing key
     */
    private Key getSigningKey() {
        String secret = environmentUtil.getProperty("JWT_SECRET", "defaultSecretKeyThatShouldBeReplacedInProduction");
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extract username from token
     *
     * @param token the JWT token
     * @return the username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from token
     *
     * @param token the JWT token
     * @return the expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract a claim from token
     *
     * @param token          the JWT token
     * @param claimsResolver the claims resolver function
     * @param <T>            the type of the claim
     * @return the claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from token
     *
     * @param token the JWT token
     * @return all claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Check if token is expired
     *
     * @param token the JWT token
     * @return true if expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validate token
     *
     * @param token the JWT token
     * @param user  the user to validate against
     * @return true if valid, false otherwise
     */
    public Boolean validateToken(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getEmail()) && !isTokenExpired(token));
    }
} 