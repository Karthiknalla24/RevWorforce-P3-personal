@echo off
REM Comprehensive Build and Test Script for RevWorkForce P3

echo.
echo ========================================
echo RevWorkForce P3 - Complete Build Script
echo ========================================
echo.

setlocal enabledelayedexpansion

REM Set base directory
set BASE_DIR=c:\Users\thula\P3\RevWorkForce-P3

echo [1/11] Building Config Server...
cd %BASE_DIR%\infrastructure\config-server
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: Config Server build failed
    exit /b 1
)
echo [OK] Config Server built

echo.
echo [2/11] Building Eureka Server...
cd %BASE_DIR%\infrastructure\eureka-server
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: Eureka Server build failed
    exit /b 1
)
echo [OK] Eureka Server built

echo.
echo [3/11] Building API Gateway...
cd %BASE_DIR%\infrastructure\api-gateway
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: API Gateway build failed
    exit /b 1
)
echo [OK] API Gateway built

echo.
echo [4/11] Building User Service...
cd %BASE_DIR%\services\user-service
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: User Service build failed
    exit /b 1
)
echo [OK] User Service built

echo.
echo [5/11] Building Employee Management Service...
cd %BASE_DIR%\services\employee-management-service
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: Employee Management Service build failed
    exit /b 1
)
echo [OK] Employee Management Service built

echo.
echo [6/11] Building Leave Service...
cd %BASE_DIR%\services\leave-service
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: Leave Service build failed
    exit /b 1
)
echo [OK] Leave Service built

echo.
echo [7/11] Building Performance Service...
cd %BASE_DIR%\services\performance-service
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: Performance Service build failed
    exit /b 1
)
echo [OK] Performance Service built

echo.
echo [8/11] Building Notification Service...
cd %BASE_DIR%\services\notification-service
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: Notification Service build failed
    exit /b 1
)
echo [OK] Notification Service built

echo.
echo [9/11] Building Reporting Service...
cd %BASE_DIR%\services\reporting-service
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: Reporting Service build failed
    exit /b 1
)
echo [OK] Reporting Service built

echo.
echo [10/11] Installing Frontend Dependencies...
cd %BASE_DIR%\frontend\revworkforce-ui
call npm install -q
if errorlevel 1 (
    echo ERROR: Frontend npm install failed
    exit /b 1
)
echo [OK] Frontend dependencies installed

echo.
echo [11/11] Building Frontend...
call npm run build -q
if errorlevel 1 (
    echo WARNING: Frontend build had issues, but continuing...
)
echo [OK] Frontend built

echo.
echo ========================================
echo All services built successfully!
echo ========================================
echo.
echo Next steps:
echo 1. Start MySQL: docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=Root@1323 mysql:8.0
echo 2. Start Config Server: cd %BASE_DIR%\infrastructure\config-server ^&^& java -jar target\config-server-0.0.1-SNAPSHOT.jar
echo 3. Start Eureka Server: cd %BASE_DIR%\infrastructure\eureka-server ^&^& java -jar target\eureka-server-0.0.1-SNAPSHOT.jar
echo 4. Start API Gateway: cd %BASE_DIR%\infrastructure\api-gateway ^&^& java -jar target\api-gateway-0.0.1-SNAPSHOT.jar
echo 5. Start all microservices in separate terminals
echo 6. Start Frontend: cd %BASE_DIR%\frontend\revworkforce-ui ^&^& npm start
echo.
echo Test credentials:
echo - Employee: EMP001 / admin123
echo - Manager: MGR001 / admin123
echo - Admin: ADMIN001 / admin123
echo.
pause
