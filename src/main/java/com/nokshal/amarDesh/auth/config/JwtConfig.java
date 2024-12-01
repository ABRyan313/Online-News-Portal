package com.nokshal.amarDesh.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value("${jwt.header}")
    private String header;

    public String getSecretKey() {
        return secretKey;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public String getHeader() {
        return header;
    }
}

