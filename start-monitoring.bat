@echo off
echo Starting RevWorkForce Monitoring Stack (ELK, Prometheus, Grafana)...

:: Check if network exists, if not create it
docker network inspect revworkforce-network >nul 2>&1
if %errorlevel% neq 0 (
    echo Creating revworkforce-network...
    docker network create revworkforce-network
)

cd monitoring
docker-compose -f docker-compose-monitoring.yml up -d

echo Monitoring Stack is up:
echo Kibana: http://localhost:5601
echo Prometheus: http://localhost:9090
echo Grafana: http://localhost:3000 (admin/admin)
echo SonarQube: http://localhost:9000 (admin/admin)
cd ..
