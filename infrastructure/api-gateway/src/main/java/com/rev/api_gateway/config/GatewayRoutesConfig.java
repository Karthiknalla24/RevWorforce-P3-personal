package com.rev.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        return builder.routes()

                // USER SERVICE
                .route("user-service", r -> r
                        .path("/api/auth/**", "/api/users/**")
                        .filters(f -> f.circuitBreaker(c ->
                                c.setName("userServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/user")))
                        .uri("lb://USER-SERVICE"))
                .route("user-service-swagger", r -> r
                        .path("/user-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/user-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://USER-SERVICE"))
                .route("employee-service-swagger", r -> r
                        .path("/employee-management-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/employee-management-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://EMPLOYEE-MANAGEMENT-SERVICE"))
                .route("leave-service-swagger", r -> r
                        .path("/leave-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/leave-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://LEAVE-SERVICE"))
                .route("notification-service-swagger", r -> r
                        .path("/notification-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/notification-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://NOTIFICATION-SERVICE"))
                .route("performance-service-swagger", r -> r
                        .path("/performance-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/performance-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://PERFORMANCE-SERVICE"))
                .route("reporting-service-swagger", r -> r
                        .path("/reporting-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/reporting-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://REPORTING-SERVICE"))


                // EMPLOYEE MANAGEMENT SERVICE
                .route("employee-service", r -> r
                        .path("/api/departments/**", "/api/designations/**", "/api/announcements/**")
                        .filters(f -> f.circuitBreaker(c ->
                                c.setName("employeeServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/employee")))
                        .uri("lb://EMPLOYEE-MANAGEMENT-SERVICE"))

                // LEAVE SERVICE
                .route("leave-service", r -> r
                        .path("/api/leaves/**", "/api/leave-types/**")
                        .filters(f -> f.circuitBreaker(c ->
                                c.setName("leaveServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/leave")))
                        .uri("lb://LEAVE-SERVICE"))

                // PERFORMANCE SERVICE
                .route("performance-service", r -> r
                        .path("/api/performance/**", "/api/goals/**")
                        .filters(f -> f.circuitBreaker(c ->
                                c.setName("performanceServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/performance")))
                        .uri("lb://PERFORMANCE-SERVICE"))

                // NOTIFICATION SERVICE
                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .filters(f -> f.circuitBreaker(c ->
                                c.setName("notificationServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/notification")))
                        .uri("lb://NOTIFICATION-SERVICE"))

                // REPORTING SERVICE
                .route("reporting-service", r -> r
                        .path("/api/dashboard/**", "/api/reports/**", "/api/activity/**")
                        .filters(f -> f.circuitBreaker(c ->
                                c.setName("reportingServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/report")))
                        .uri("lb://REPORTING-SERVICE"))

                .build();
    }
}