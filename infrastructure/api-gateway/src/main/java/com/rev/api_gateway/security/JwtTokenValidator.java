package com.rev.api_gateway.security;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenValidator {

    private final JwtUtil jwtUtil;

    public JwtTokenValidator(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public boolean validateToken(String token) {

        try {

            if (jwtUtil.isTokenExpired(token)) {
                return false;
            }

            jwtUtil.extractClaims(token);

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}