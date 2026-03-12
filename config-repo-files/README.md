# RevWorkForce Config Repository

Upload all `.properties` files to your GitHub config repository.

## Files to Upload

1. `user-service.properties`
2. `notification-service.properties`
3. `employee-management-service.properties`
4. `leave-service.properties`
5. `performance-service.properties`
6. `reporting-service.properties`
7. `api-gateway.properties`

## Environment Variable Required

Set MySQL password as environment variable:

```bash
# Windows
set MYSQL_PASSWORD=your_password

# Linux/Mac
export MYSQL_PASSWORD=your_password
```

## Config Server Setup

Your Config Server should point to the GitHub repository containing these files:

```properties
spring.cloud.config.server.git.uri=https://github.com/your-username/revworkforce-config
spring.cloud.config.server.git.default-label=main
```

## Service Configuration

Each microservice has local `application.properties` with only:

```properties
spring.application.name=<service-name>
spring.config.import=optional:configserver:http://localhost:8888
```

## Startup Order

1. Config Server (port 8888)
2. Eureka Server (port 8761)
3. All microservices
4. API Gateway (port 8080)
