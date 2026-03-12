# Rebuild all services - PowerShell version
# Run with: .\rebuild-all.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "RevWorkForce P3 - Complete Build Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$baseDir = "c:\Users\thula\P3\RevWorkForce-P3"

# Function to build service
function Build-Service {
    param(
        [string]$name,
        [string]$path
    )
    
    Write-Host "[$(Get-Date -Format 'HH:mm:ss')] Building $name..." -ForegroundColor Yellow
    Push-Location $path
    & mvn clean package -DskipTests -q
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[OK] $name built successfully" -ForegroundColor Green
    } else {
        Write-Host "[ERROR] $name build failed" -ForegroundColor Red
        exit 1
    }
    Pop-Location
    Write-Host ""
}

# Build all services
Build-Service "Config Server" "$baseDir\infrastructure\config-server"
Build-Service "Eureka Server" "$baseDir\infrastructure\eureka-server"
Build-Service "API Gateway" "$baseDir\infrastructure\api-gateway"
Build-Service "User Service" "$baseDir\services\user-service"
Build-Service "Employee Management Service" "$baseDir\services\employee-management-service"
Build-Service "Leave Service" "$baseDir\services\leave-service"
Build-Service "Performance Service" "$baseDir\services\performance-service"
Build-Service "Notification Service" "$baseDir\services\notification-service"
Build-Service "Reporting Service" "$baseDir\services\reporting-service"

Write-Host "========================================" -ForegroundColor Green
Write-Host "All services built successfully!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "1. Start MySQL: docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=Root@1323 mysql:8.0" -ForegroundColor White
Write-Host "2. Start Config Server: cd $baseDir\infrastructure\config-server; java -jar target\config-server-0.0.1-SNAPSHOT.jar" -ForegroundColor White
Write-Host "3. Start Eureka Server: cd $baseDir\infrastructure\eureka-server; java -jar target\eureka-server-0.0.1-SNAPSHOT.jar" -ForegroundColor White
Write-Host "4. Start API Gateway: cd $baseDir\infrastructure\api-gateway; java -jar target\api-gateway-0.0.1-SNAPSHOT.jar" -ForegroundColor White
Write-Host "5. Start all microservices in separate terminals" -ForegroundColor White
Write-Host "6. Start Frontend: cd $baseDir\frontend\revworkforce-ui; npm start" -ForegroundColor White
