package Adventure_generator.Util;

/**
 * JWT Utility
 * 
 * Utility class for JSON Web Token (JWT) generation, validation, and parsing.
 * Uses HS512 algorithm with 512-bit secret key for token signing.
 * 
 * Features:
 * - JWT token generation with user data
 * - Token validation (signature and expiration)
 * - Claims extraction (username, expiration date)
 * - Custom token expiry support
 * 
 * Token Configuration:
 * - Algorithm: HS512 (HMAC with SHA-512)
 * - Validity: 7 days (604800 seconds)
 * - Subject: Username
 * - Secret: Configured in application.properties
 * 
 * Security:
 * - 512+ bit secret key required for HS512
 * - Token expiration validation
 * - Secure key generation from configured secret
 * 
 * @author Astra K. Nguyen
 * @version 1.0.0
 * @since 2026-01-28
 */

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import Adventure_generator.DTOs.Response.UserData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JwtUtil implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 7 * 24 * 60 * 60; // 7 days in seconds

    @Value("${jwt.secret}")
    private String secret;

    // Generate token
    public String generateToken(UserData userData){
        Map<String,Object> claims = new HashMap<>();
        return doGenerateToken(claims, userData.getUserName(),JWT_TOKEN_VALIDITY);        
    }
    private String doGenerateToken(Map<String,Object> claim,  String userName, long expiryMillis){
        return Jwts.builder()
            .setClaims(claim)
            .setSubject(userName) // User's identity 
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiryMillis * 1000))
            .signWith(getSigningKey(), SignatureAlgorithm.HS512) 
            .compact(); 
    }
    private SecretKey getSigningKey(){
        // Convert secret string into byte array  
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8); 
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
        
    }
    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    public Boolean validateToken(String token, String username){
        final String userName = getUsernameFromToken(token);
        return (userName.equals(userName) && !isTokenExpired(token));
    }

    // Secret key is needed 
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody();
    }
    // Extract any type of claim within token 
    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolverFunction ){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolverFunction.apply(claims);
    }
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public String generateTokenWithCustomExpiry(UserData userData, long expiryMillis){
        return doGenerateToken(new HashMap<>(), userData.getUserName(), expiryMillis);
    }
}
