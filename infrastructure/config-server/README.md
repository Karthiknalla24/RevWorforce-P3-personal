# Config Server - RevWorkForce Infrastructure

## Overview
The **Config Server** provides a centralized way to manage configuration properties for all RevWorkForce microservices. It's configured to search for configuration files in a "native" folder, which is the local repository on this machine.

---

## File: `ConfigServerApplication.java`
This is the main entry point of the Spring Cloud Config Server.

```java
1: package com.rev.config_server; // Declares the package where this class lives.
2: 
3: import org.springframework.boot.SpringApplication; // Imports the utility to launch the Spring application.
4: import org.springframework.boot.autoconfigure.SpringBootApplication; // Imports the annotation for basic Spring Boot configuration.
5: import org.springframework.cloud.config.server.EnableConfigServer; // Imports the annotation to turn this Spring Boot app into a Config Server.
6: 
7: @EnableConfigServer // Activation: Tells Spring Cloud to enable the configuration server features.
8: @SpringBootApplication // Core: Combines @Configuration, @EnableAutoConfiguration, and @ComponentScan.
9: public class ConfigServerApplication { // The class definition for the application.
10: 
11: 	public static void main(String[] args) { // The standard main method that Java uses to start the program.
12: 		SpringApplication.run(ConfigServerApplication.class, args); // Boots up the entire Spring Framework and the Config Server.
13: 	}
14: 
15: }
```

---

## File: `application.properties`
This file contains the configuration settings for the Config Server itself.

```properties
1: server.port=8888 // The server listens on the standard port 8888 for configuration requests.
2: spring.application.name=config-server // Sets the service name to "config-server" within the microservices ecosystem.
3: 
4: spring.profiles.active=native // Sets the active profile to "native", meaning the server will look at the local filesystem.
5: spring.cloud.config.server.native.search-locations=file:///C:/P3-ANT-SAFE/RevWorkForce-P3/config-repo-files/ // Specifies the local folder where configuration files (.yml, .properties) for other services are stored.
```

---

## How to Run
1. Navigate to the `infrastructure/config-server` directory.
2. Ensure the `config-repo-files` directory exists in the specified path.
3. Run `./mvnw spring-boot:run` (or `mvnw.cmd` on Windows).

## Config Repositories
The configuration server fetches configuration for each microservice dynamically based on their names. For example, if a service named `user-service` starts up, it will request its configuration from this server at `http://localhost:8888/user-service/default`.
