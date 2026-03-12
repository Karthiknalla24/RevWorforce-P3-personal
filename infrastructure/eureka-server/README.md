# Eureka Server - RevWorkForce Infrastructure

## Overview
This service acts as the **Service Registry** for the RevWorkForce microservices architecture. It allows all microservices to discover each other dynamically without hardcoding their IP addresses or port numbers.

---

## File: `EurekaServerApplication.java`
This is the main entry point of the Eureka Server application.

```java
1: package com.rev.eureka_server; // Defines the package where this class belongs.
2: 
3: import org.springframework.boot.SpringApplication; // Imports the utility to launch the Spring application.
4: import org.springframework.boot.autoconfigure.SpringBootApplication; // Imports the annotation for basic Spring Boot configuration.
5: import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer; // Imports the annotation to turn this Spring Boot app into a Eureka Server.
6: 
7: @EnableEurekaServer // Activation: Tells Spring Cloud to enable the Eureka Registry server features.
8: @SpringBootApplication // Core: Combines @Configuration, @EnableAutoConfiguration, and @ComponentScan.
9: public class EurekaServerApplication { // The class definition for the application.
10: 
11: 	public static void main(String[] args) { // The standard main method that Java uses to start the program.
12: 		SpringApplication.run(EurekaServerApplication.class, args); // Boots up the entire Spring Framework and the Eureka Server.
13: 	}
14: 
15: }
```

---

## File: `application.properties`
This file contains the configuration settings for the Eureka Server.

```properties
1: server.port=8761 // Sets the server to listen on port 8761 (default port for Eureka).
2: spring.application.name=eureka-server // Names this service as "eureka-server" for identification.
3: 
4: eureka.client.register-with-eureka=false // Since this is the server itself, it shouldn't try to register with another Eureka instance.
5: eureka.client.fetch-registry=false // Since this is the source of truth, it doesn't need to fetch a registry from elsewhere.
6: eureka.server.enable-self-preservation=false // Disables self-preservation mode to allow for faster removal of stale service instances during development.
```

---

## How to Run
1. Navigate to the `infrastructure/eureka-server` directory.
2. Run `./mvnw spring-boot:run` (or `mvnw.cmd` on Windows).
3. Access the Eureka dashboard at `http://localhost:8761`.

## Key Dependencies (`pom.xml`)
- `spring-cloud-starter-netflix-eureka-server`: The core dependency that provides the Service Registry functionality.
