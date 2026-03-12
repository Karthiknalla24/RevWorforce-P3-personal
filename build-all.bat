@echo off
echo ========================================
echo Building All Services for Docker
echo ========================================
echo.

echo [1/9] Building Config Server...
cd infrastructure\config-server
call mvn clean package -DskipTests
cd ..\..

echo [2/9] Building Eureka Server...
cd infrastructure\eureka-server
call mvn clean package -DskipTests
cd ..\..

echo [3/9] Building API Gateway...
cd infrastructure\api-gateway
call mvn clean package -DskipTests
cd ..\..

echo [4/9] Building User Service...
cd services\user-service
call mvn clean package -DskipTests
cd ..\..

echo [5/9] Building Notification Service...
cd services\notification-service
call mvn clean package -DskipTests
cd ..\..

echo [6/9] Building Employee Management Service...
cd services\employee-management-service
call mvn clean package -DskipTests
cd ..\..

echo [7/9] Building Leave Service...
cd services\leave-service
call mvn clean package -DskipTests
cd ..\..

echo [8/9] Building Performance Service...
cd services\performance-service
call mvn clean package -DskipTests
cd ..\..

echo [9/9] Building Reporting Service...
cd services\reporting-service
call mvn clean package -DskipTests
cd ..\..

echo.
echo ========================================
echo All services built successfully!
echo Now run: docker-compose up -d
echo ========================================
pause
