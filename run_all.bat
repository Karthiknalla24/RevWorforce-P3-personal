@echo off
setlocal

echo ==========================================
echo   RevWorkForce-P3 - Start All Services
echo ==========================================

REM 1. Config Server (MUST be first)
echo [1/9] Starting Config Server...
start "config-server" cmd /c "cd /d infrastructure\config-server && mvn spring-boot:run"
timeout /t 10 /nobreak > nul

REM 2. Eureka Server
echo [2/9] Starting Eureka Server...
start "eureka-server" cmd /c "cd /d infrastructure\eureka-server && mvn spring-boot:run"
timeout /t 10 /nobreak > nul

REM 3. API Gateway
echo [3/9] Starting API Gateway...
start "api-gateway" cmd /c "cd /d infrastructure\api-gateway && mvn spring-boot:run"

REM 4. User Service
echo [4/9] Starting User Service...
start "user-service" cmd /c "cd /d services\user-service && mvn spring-boot:run"

REM 5. Employee Management Service
echo [5/9] Starting Employee Management Service...
start "employee-mgmt-service" cmd /c "cd /d services\employee-management-service && mvn spring-boot:run"

REM 6. Leave Service
echo [6/9] Starting Leave Service...
start "leave-service" cmd /c "cd /d services\leave-service && mvn spring-boot:run"

REM 7. Notification Service
echo [7/9] Starting Notification Service...
start "notification-service" cmd /c "cd /d services\notification-service && mvn spring-boot:run"

REM 8. Performance Service
echo [8/9] Starting Performance Service...
start "performance-service" cmd /c "cd /d services\performance-service && mvn spring-boot:run"

REM 9. Reporting Service
echo [9/9] Starting Reporting Service...
start "reporting-service" cmd /c "cd /d services\reporting-service && mvn spring-boot:run"

echo ==========================================
echo   All window triggers sent. 
echo   Please check individual windows for logs.
echo ==========================================
pause
