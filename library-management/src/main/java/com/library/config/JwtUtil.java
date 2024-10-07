// src/main/java/com/library/config/JwtUtil.java
package com.library.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    // Generate JWT token
    public String generateToken(String username){
        return Jwts.builder()
				.subject(username)
				.issuedAt(new Date())
				.expiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
				.compact();
    }

    // Get username from JWT token
    

	public String getUsernameFromToken(String token){
        return Jwts.parser().verifyWith((SecretKey) Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))).build().parseSignedClaims(token).getPayload().getSubject();
    }

    // Validate JWT token
    public boolean validateJwtToken(String token){
        try {
        	Jwts.parser().verifyWith((SecretKey) Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))).build().parseSignedClaims(token);
			return true;
        } catch (MalformedJwtException e){
            System.err.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e){
            System.err.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e){
            System.err.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e){
            System.err.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

	
}
