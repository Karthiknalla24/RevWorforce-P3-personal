@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Starting RevWorkForce complete setup
echo ========================================
echo.

REM Check if MySQL is running
netstat -an | findstr "3306" >nul
if errorlevel 1 (
    echo [WARNING] MySQL is not running on port 3306!
    echo Please start MySQL first. Trying to continue anyway...
    echo.
)

set MYSQL_PASSWORD=Root@1323

echo [1/10] Starting Infrastructure: Config Server...
start "Config Server" cmd /k "cd infrastructure\config-server && mvn spring-boot:run"
timeout /t 30 /nobreak

echo [2/10] Starting Infrastructure: Eureka Server...
start "Eureka Server" cmd /k "cd infrastructure\eureka-server && mvn spring-boot:run"
timeout /t 30 /nobreak

echo [3/10] Starting Infrastructure: API Gateway...
start "API Gateway" cmd /k "cd infrastructure\api-gateway && mvn spring-boot:run"
timeout /t 20 /nobreak


echo [4/10] Starting Service: User Service...
start "User Service" cmd /k "cd services\user-service && mvn spring-boot:run"
timeout /t 15 /nobreak

echo [5/10] Starting Service: Notification Service...
start "Notification Service" cmd /k "cd services\notification-service && mvn spring-boot:run"
timeout /t 10 /nobreak

echo [6/10] Starting Service: Employee Management Service...
start "Employee Management Service" cmd /k "cd services\employee-management-service && mvn spring-boot:run"
timeout /t 10 /nobreak

echo [7/10] Starting Service: Leave Service...
start "Leave Service" cmd /k "cd services\leave-service && mvn spring-boot:run"
timeout /t 10 /nobreak

echo [8/10] Starting Service: Performance Service...
start "Performance Service" cmd /k "cd services\performance-service && mvn spring-boot:run"
timeout /t 10 /nobreak

echo [9/10] Starting Service: Reporting Service...
start "Reporting Service" cmd /k "cd services\reporting-service && mvn spring-boot:run"
timeout /t 10 /nobreak


echo [10/10] Starting Frontend: Angular UI...
start "RevWorkforce UI" cmd /k "cd frontend\revworkforce-ui && npm start"

echo.
echo ========================================
echo All services are starting...
echo ========================================
echo.
echo Dashboard/Links:
echo ----------------------------------------
echo Config Server:      http://localhost:8888
echo Eureka Dashboard:   http://localhost:8761
echo API Gateway:        http://localhost:8080
echo Frontend Platform:  http://localhost:4200
echo.
echo Microservice Ports:
echo ----------------------------------------
echo User Service:       8081
echo Leave Service:      8082
echo Performance Service: 8083
echo Employee Mgmt:      8084
echo Notification:       8085
echo Reporting:          8086
echo.
echo Use stop-all.bat to shut down all processes.
echo.
pause
