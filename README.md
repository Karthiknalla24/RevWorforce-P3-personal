# RevWorkForce Microservices Platform

## Architecture Overview
RevWorkForce is a scalable, cloud-native Human Resource Management System (HRMS) built on a microservices architecture. It features a robust Spring Cloud stack for infrastructure and specialized business services for workforce management.

## 🏗️ Infrastructure Services
*   **[Eureka Server](./infrastructure/eureka-server)**: Service discovery and registration.
*   **[Config Server](./infrastructure/config-server)**: Centralized configuration management.
*   **[API Gateway](./infrastructure/api-gateway)**: Secure entry point with JWT Auth, Rate Limiting, and Circuit Breakers.
*   **[Monitoring Stack](./monitoring)**: Real-time observability using Prometheus, Grafana, and ELK.

## 💼 Business Services
*   **[User Service](./services/user-service)**: Authentication, Roles, and User Profiles.
*   **[Employee Management Service](./services/employee-management-service)**: Departments, Designations, and Announcements.
*   **[Leave Service](./services/leave-service)**: Leave applications, balances, and global holidays.
*   **[Performance Service](./services/performance-service)**: Appraisals, manager feedback, and goal tracking.
*   **[Notification Service](./services/notification-service)**: Real-time user alerts and system broadcasts.
*   **[Reporting Service](./services/reporting-service)**: Data aggregation, dashboard stats, and audit logging.

## 🚀 Getting Started
1.  **Database**: Initialize the system via `init-db.sql`.
2.  **Infrastructure**: Start Eureka Server (8761), then Config Server (8888).
3.  **Gateway**: Start API Gateway (8080).
4.  **Services**: Start all business services in any order.
5.  **Frontend**: Run the Angular application in the `frontend` directory.

## 🛠️ Tech Stack
*   **Backend**: Java 17, Spring Boot 3.2.2, Spring Cloud 2023.
*   **Persistence**: MySQL, Spring Data JPA.
*   **Security**: JWT, Spring Security.
*   **Observability**: Spring Boot Actuator, Prometheus, Grafana, ELK.
*   **Testing**: JUnit 5, Mockito (100% Mocked Unit Tests).
