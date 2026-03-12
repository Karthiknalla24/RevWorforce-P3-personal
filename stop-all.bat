@echo off
echo ========================================
echo Stopping All RevWorkForce Services
echo ========================================
echo.

echo Stopping all Java processes (Spring Boot services)...
taskkill /F /FI "WINDOWTITLE eq Config Server*" 2>nul
taskkill /F /FI "WINDOWTITLE eq Eureka Server*" 2>nul
taskkill /F /FI "WINDOWTITLE eq User Service*" 2>nul
taskkill /F /FI "WINDOWTITLE eq Notification Service*" 2>nul
taskkill /F /FI "WINDOWTITLE eq Employee Management Service*" 2>nul
taskkill /F /FI "WINDOWTITLE eq Leave Service*" 2>nul
taskkill /F /FI "WINDOWTITLE eq Performance Service*" 2>nul
taskkill /F /FI "WINDOWTITLE eq Reporting Service*" 2>nul
taskkill /F /FI "WINDOWTITLE eq API Gateway*" 2>nul

echo Stopping Frontend (Node/Angular)...
taskkill /F /FI "WINDOWTITLE eq RevWorkforce UI*" 2>nul

echo Killing any orphaned Java processes on service ports...
for %%p in (8888 8761 8080 8081 8082 8083 8084 8085 8086) do (
    for /f "tokens=5" %%a in ('netstat -aon ^| findstr :%%p ^| findstr LISTENING') do (
        taskkill /F /PID %%a 2>nul
    )
)

echo Killing any orphaned Node processes on 4200...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :4200 ^| findstr LISTENING') do (
    taskkill /F /PID %%a 2>nul
)

echo.
echo All services stopped!
echo.
pause
