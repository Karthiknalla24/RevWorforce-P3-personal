package com.rev.api_gateway.config;

import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SecurityConfig {

    public static final List<String> PUBLIC_ROUTES = List.of(

            "/api/auth/login",
            "/api/auth/register",

            "/actuator",
            "/actuator/**",

            // Swagger endpoints
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui",
            "/swagger-ui/**",
            "/swagger-ui.html",

            // Gateway Swagger routes
            "/user-service/v3/api-docs",
            "/employee-management-service/v3/api-docs",
            "/leave-service/v3/api-docs",
            "/notification-service/v3/api-docs",
            "/performance-service/v3/api-docs",
            "/reporting-service/v3/api-docs"
    );

    public static boolean isPublicRoute(String path) {
        return PUBLIC_ROUTES.stream().anyMatch(route -> 
            path.equals(route) || path.startsWith(route + "/")
        );
    }
}