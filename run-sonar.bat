@echo off
echo ========================================================
echo   RevWorkForce - Local SonarQube 9.9 Analysis (Token)
echo ========================================================
echo.
echo Make sure SonarQube is running at http://localhost:9000
echo using the security token provided.
echo.

:: SonarQube Security Token
set SONAR_TOKEN=squ_77d7a794b3cf6981ac172a48258e10b019a6e56a

set SERVICES=infrastructure\config-server infrastructure\eureka-server infrastructure\api-gateway services\user-service services\notification-service services\employee-management-service services\leave-service services\performance-service services\reporting-service

for %%s in (%SERVICES%) do (
    echo.
    echo --------------------------------------------------------
    echo Analyzing Service: %%s
    echo --------------------------------------------------------
    cd %%s
    :: Analysis using the security token for authentication
    call mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=%SONAR_TOKEN%
    cd ..\..
)

echo.
echo ========================================================
echo   Analysis Complete! 
echo   View results: http://localhost:9000
echo ========================================================
pause
