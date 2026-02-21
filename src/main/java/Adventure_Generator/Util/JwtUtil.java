package Adventure_generator.Util;

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

/**
 * JWT (JSON Web Token) utility for stateless authentication.
 * 
 * Provides cryptographic token generation and validation for user authentication.
 * Tokens are signed with HMAC-SHA512 and contain user identity claims.
 * 
 * TOKEN LIFECYCLE:
 * 1. User logs in → Server generates JWT with user data
 * 2. Client receives token → Stores in localStorage/sessionStorage
 * 3. Every API request → Client includes token in Authorization header
 * 4. Server validates token → Extracts username, checks signature & expiration
 * 5. Token expires → Client must re-authenticate
 * 
 * SECURITY CONSIDERATIONS:
 * - Secret key MUST be 512+ bits for HS512 algorithm
 * - Tokens are stateless - server doesn't track them
 * - Cannot revoke tokens before expiration (use short TTL for sensitive apps)
 * - Store secret in environment variables, never commit to source control
 * - Client should store tokens securely (HttpOnly cookies > localStorage for XSS protection)
 * 
 * TOKEN STRUCTURE (JWT):
 * - Header: Algorithm and token type {"alg": "HS512", "typ": "JWT"}
 * - Payload: User claims {"sub": "username", "iat": 1234567890, "exp": 1234999999}
 * - Signature: HMACSHA512(base64(header) + "." + base64(payload), secret)
 * 
 * CONFIGURATION:
 * - Token validity: 30 days (configurable via JWT_TOKEN_VALIDITY)
 * - Algorithm: HS512 (HMAC with SHA-512)
 * - Secret: Injected from application.properties (jwt.secret)
 * 
 * @see UserData
 * @see io.jsonwebtoken.Jwts
 */
@Component
public class JwtUtil implements Serializable {
    
    private static final long serialVersionUID = -2550185165626007488L;
    
    /**
     * Default token validity period: 30 days in milliseconds.
     * 
     * PRODUCTION RECOMMENDATION:
     * - Public apps: 15 minutes - 1 hour (implement refresh tokens)
     * - Internal apps: 1-7 days
     * - Low-security apps: 30 days (current setting)
     * 
     * Longer expiration = more convenience, less security
     */
    public static final long JWT_TOKEN_VALIDITY = 30L * 24 * 60 * 60 * 1000; // 30 days

    /**
     * Secret key for signing tokens, injected from application.properties.
     * 
     * CRITICAL: Must be at least 512 bits (64 characters) for HS512.
     * Use strong random key: openssl rand -base64 64
     * 
     * Example application.properties:
     * jwt.secret=YOUR_SUPER_SECRET_KEY_HERE_AT_LEAST_512_BITS_LONG
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Generates a JWT token for authenticated user with default 30-day expiration.
     * 
     * TOKEN STORAGE (Client-Side):
     * Frontend should store this token in:
     * 1. localStorage.setItem('jwtToken', token) - Simple but vulnerable to XSS
     * 2. sessionStorage - Cleared on tab close
     * 3. HttpOnly cookie (recommended) - Not accessible via JavaScript, prevents XSS
     * 
     * USAGE:
     * Client must include token in every API request:
     * Authorization: Bearer <token>
     * 
     * @param userData User information to embed in token (username used as subject)
     * @return Signed JWT token string (e.g., "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ...")
     */
    public String generateToken(UserData userData){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userData.getUserName(), JWT_TOKEN_VALIDITY);        
    }
    
    /**
     * Internal method to construct and sign JWT token.
     * 
     * TOKEN COMPONENTS:
     * - Claims: Custom key-value pairs (currently empty, can add roles/permissions)
     * - Subject: Username (primary identifier)
     * - IssuedAt: Token creation timestamp
     * - Expiration: Token expiry timestamp (current time + validity period)
     * - Signature: HMAC-SHA512 signature using secret key
     * 
     * @param claims Additional custom claims to include in token payload
     * @param subject Username to set as token subject
     * @param expiryMillis Token validity duration in milliseconds
     * @return Compact serialized JWT string
     */
    private String doGenerateToken(Map<String, Object> claims, String subject, long expiryMillis){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * Creates SecretKey instance from secret string for token signing and validation.
     * 
     * SECURITY NOTE:
     * - Converts UTF-8 string to byte array
     * - Secret MUST be 512+ bits for HS512 (recommended: 64+ characters)
     * - Weak secrets can be brute-forced, compromising all tokens
     * 
     * @return SecretKey instance for token signing and validation
     */
    private SecretKey getSigningKey(){
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8); 
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }
    
    /**
     * Parses and extracts all claims from JWT token payload.
     * 
     * SECURITY:
     * - Validates signature using secret key (prevents token tampering)
     * - Throws JwtException if signature is invalid or token is malformed
     * - Requires exact secret key that was used to sign the token
     * 
     * @param token JWT token string to parse
     * @return Claims object containing all token payload data
     * @throws io.jsonwebtoken.JwtException if token is invalid or signature doesn't match
     */
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * Generic claim extractor using functional interface.
     * 
     * Allows extracting any claim type using lambda/method reference:
     * - Claims::getSubject → username
     * - Claims::getExpiration → expiry date
     * - Claims::getIssuedAt → creation date
     * - claim -> claim.get("customKey") → custom claims
     * 
     * @param <T> Return type of the claim
     * @param token JWT token to extract claim from
     * @param claimsResolver Function to apply to Claims object
     * @return Extracted claim value of type T
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Extracts username from JWT token subject claim.
     * 
     * This is the primary user identifier stored during token generation.
     * Used by JwtAuthenticationFilter to identify the authenticated user.
     * 
     * @param token JWT token string
     * @return Username extracted from token subject
     */
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    /**
     * Extracts expiration date from JWT token.
     * 
     * @param token JWT token string
     * @return Expiration timestamp as Date object
     */
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    /**
     * Checks if token has expired based on current system time.
     * 
     * @param token JWT token string
     * @return true if token expiration date is before current time, false otherwise
     */
    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    /**
     * Validates JWT token signature and expiration.
     * 
     * VALIDATION STEPS:
     * 1. Extract username from token subject claim
     * 2. Verify token signature using secret key (prevents tampering)
     * 3. Compare extracted username with expected username
     * 4. Check expiration timestamp
     * 
     * Used by JwtAuthenticationFilter to authenticate requests.
     * 
     * @param token JWT token to validate
     * @param username Expected username to match against token subject
     * @return true if token is valid, signature matches, username matches, and not expired
     */
    public Boolean validateToken(String token, String username){
        final String userName = getUsernameFromToken(token);
        return (userName.equals(username) && !isTokenExpired(token));
    }
    
    /**
     * Generates JWT token with custom expiration duration.
     * 
     * USE CASES:
     * - "Remember Me" feature: longer expiration (30 days)
     * - Temporary access: shorter expiration (15 minutes)
     * - Refresh tokens: very long expiration (90 days)
     * 
     * @param userData User information to embed in token
     * @param expiryMillis Custom token validity duration in milliseconds
     * @return Signed JWT token with custom expiration
     */
    public String generateTokenWithCustomExpiry(UserData userData, long expiryMillis){
        return doGenerateToken(new HashMap<>(), userData.getUserName(), expiryMillis);
    }
}
