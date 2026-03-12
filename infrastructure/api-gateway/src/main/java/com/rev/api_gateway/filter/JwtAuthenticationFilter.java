package com.rev.api_gateway.filter;

import com.rev.api_gateway.constants.SecurityConstants;
import com.rev.api_gateway.security.JwtTokenValidator;
import com.rev.api_gateway.security.JwtUtil;
import com.rev.api_gateway.util.TokenUtils;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtTokenValidator validator;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtTokenValidator validator, JwtUtil jwtUtil) {
        this.validator = validator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // Skip authentication for public endpoints
        if (isPublicRoute(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(SecurityConstants.AUTH_HEADER);

        String token = TokenUtils.extractToken(authHeader);

        if (token == null) {
            return unauthorized(exchange, "Missing Authorization Header");
        }

        try {

            if (!validator.validateToken(token)) {
                return unauthorized(exchange, "Invalid JWT Token");
            }

            String userId = jwtUtil.extractUserId(token);
            String role = jwtUtil.extractRole(token);

            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .header(SecurityConstants.USER_ID_HEADER, userId)
                    .header(SecurityConstants.USER_ROLE_HEADER, role)
                    .build();

            ServerWebExchange modifiedExchange =
                    exchange.mutate().request(request).build();

            return chain.filter(modifiedExchange);

        } catch (Exception ex) {
            return unauthorized(exchange, "Token validation failed");
        }
    }

    private boolean isPublicRoute(String path) {

        return com.rev.api_gateway.config.SecurityConfig
                .isPublicRoute(path);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

        byte[] bytes = message.getBytes();

        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(bytes)));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}