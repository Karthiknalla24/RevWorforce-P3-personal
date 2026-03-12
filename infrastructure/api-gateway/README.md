# API Gateway - RevWorkForce Infrastructure

## Overview
The **API Gateway** is the single entry point for all client requests in the RevWorkForce microservices architecture. It handles routing, security (JWT authentication), CORS configuration, and circuit breaking.

---

## File: `ApiGatewayApplication.java`
The main bootstrap class for the Spring Cloud Gateway.

```java
@SpringBootApplication // Standard Spring Boot application entry point.
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args); // Launches the gateway.
    }
}
```

---

## Configuration Files

### `GatewayRoutesConfig.java`
Defines the routing rules for the microservices.

```java
@Configuration // Marks this as a configuration class.
public class GatewayRoutesConfig {
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            // Configures routing for the User Service
            .route("user-service", r -> r
                .path("/api/auth/**", "/api/users/**") // Intercepts these paths.
                .filters(f -> f.circuitBreaker(c -> // Adds a circuit breaker.
                    c.setName("userServiceCircuitBreaker")
                        .setFallbackUri("forward:/fallback/user"))) // URI to call if user-service is down.
                .uri("lb://USER-SERVICE")) // Load balances requests to the registered "USER-SERVICE" in Eureka.

            // Similar routes exist for other services (employee-management, leave, performance, notification, reporting).
            // Swagger routes are also defined for centralized API documentation access.
            .build();
    }
}
```

### `SecurityConfig.java`
Defines which routes are public and which require authentication.

```java
@Configuration
public class SecurityConfig {
    // List of routes that don't require a token (e.g., login, register, actuator, swagger).
    public static final List<String> PUBLIC_ROUTES = List.of(
        "/api/auth/login", "/api/auth/register", "/actuator/**", "/swagger-ui/**"
    );

    // Helper method to check if a path belongs to the public routes list.
    public static boolean isPublicRoute(String path) {
        return PUBLIC_ROUTES.stream().anyMatch(route -> 
            path.equals(route) || path.startsWith(route + "/"));
    }
}
```

### `CorsConfig.java`
Handles Cross-Origin Resource Sharing (CORS) to allow the frontend to communicate with the gateway.

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*"); // Allows any origin.
        config.addAllowedHeader("*"); // Allows any header.
        config.addAllowedMethod("*"); // Allows any HTTP method.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply to all paths.
        return new CorsWebFilter(source);
    }
}
```

---

## Security & Filters

### `JwtAuthenticationFilter.java`
A global filter that intercepts every incoming request to validate the JWT token.

```java
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    // filter() method logic:
    // 1. Checks if the route is public (using SecurityConfig.isPublicRoute).
    // 2. Extracts the "Authorization" header from the request.
    // 3. Extracts and validates the JWT token using JwtTokenValidator.
    // 4. Extracts User ID and Role from the token using JwtUtil.
    // 5. Mutates the request to add custom "X-User-Id" and "X-User-Role" headers.
    // 6. Continues the filter chain with the modified request.
    // 7. If validation fails or token is missing, returns a 401 Unauthorized response.

    @Override
    public int getOrder() {
        return -1; // Ensures this filter runs before other filters.
    }
}
```

### `JwtUtil.java`
Provides utility methods for parsing the JWT token.
- `extractUserId(String token)`: Retrieves the User ID claim.
- `extractRole(String token)`: Retrieves the Role claim.
- `extractExpiration(String token)`: Checks when the token expires.

### `JwtTokenValidator.java`
Contains the core logic for verifying the digital signature of the JWT token using the secret key. It also checks if the token is properly formatted and not expired.

---

## Controllers & Exceptions

### `FallbackController.java`
Provides fallback responses (e.g., "Service is temporarily unavailable") when a microservice is unreachable.

### `GlobalExceptionHandler.java`
Intersects any exceptions occurring at the gateway level and returns a consistent Error response body.

---

## File: `application.properties`
Configuration for the gateway service itself.

```properties
server.port=8080 // Gateway runs on port 8080.
spring.application.name=api-gateway // Identification in Eureka.
spring.config.import=configserver:http://localhost:8888 // Fetches additional config from Config Server.
eureka.client.service-url.defaultZone=http://localhost:8761/eureka // Eureka server location.
spring.cloud.gateway.discovery.locator.enabled=true // Enables dynamic route discovery from Eureka.
spring.cloud.gateway.discovery.locator.lower-case-service-id=true // Uses lowercase names for service IDs in paths.
management.endpoints.web.exposure.include=* // Exposes Actuator metrics.
```

---

## How to Run
1. Ensure **Eureka Server** and **Config Server** are running first.
2. Run the API Gateway using Maven: `./mvnw spring-boot:run`.
