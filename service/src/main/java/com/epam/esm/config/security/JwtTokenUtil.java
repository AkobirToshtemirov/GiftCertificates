package com.epam.esm.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${application.security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.token.issuer}")
    private String issuer;

    @Value("${application.security.jwt.token.expiration}")
    private Long expiration;


    public String generateToken(@NonNull String username) {
        Date date = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(date)
                .setIssuer(issuer)
                .setExpiration(new Date(date.getTime() + expiration))
                .signWith(signKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValid(@NonNull String token) {
        try {
            Claims claims = getClaims(token);
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUsername(@NonNull String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key signKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }
}
