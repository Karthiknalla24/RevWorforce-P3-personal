# User Service

## Overview
The User Service is the backbone of RevWorkForce, managing user identities, authentication, roles, and the organizational hierarchy.

## Core Features & Method Implementation

### 1. User Management
Full lifecycle management of employees and admins.
*   `createUser(CreateUserRequest)`: Hashes passwords, assigns roles, and triggers leave balance initialization.
*   `updateUser(Long id, UpdateUserRequest)`: Updates profile information and manager assignments.
*   `getUserById(Long id)`: Retrieves detailed profile with manager hierarchy.
*   `deactivateUser(Long id)`: Soft-delist users from active directories.

### 2. Authentication (AuthService)
Secure portal access and token generation.
*   `login(LoginRequest)`: Validates credentials and returns a JWT token.
*   `changePassword(Long userId, String old, String new)`: Secure password update logic.

### 3. Service Interactions (OpenFeign)
*   **Leave Service**: Automatically initializes leave balances for new employees.
*   **Reporting Service**: Logs login and registration activities.

### 4. Logging Aspect (AOP)
*   `LoggingAspect.java`: Intercepts all controller and service calls for standardized logging of parameters, return values, and execution time.

## Tech Stack
*   **Spring Data JPA**: MySQL persistence.
*   **BCrypt**: Password encryption.
*   **JJWT**: Token management.
