@echo off
echo ========================================================
echo Iniciando OmniTrack Logistics - 4 Microservicios...
echo ========================================================

start "Fleet-Management-Service (8081)" cmd /k "java -jar fleet-management-service\target\fleet-management-service-1.0.0.jar"
start "GPS-Ingestion-Service (8082)" cmd /k "java -jar gps-ingestion-service\target\gps-ingestion-service-1.0.0.jar"
start "Route-Calculation-Service (8083)" cmd /k "java -jar route-calculation-service\target\route-calculation-service-1.0.0.jar"
start "Alert-Service (8084)" cmd /k "java -jar alert-service\target\alert-service-1.0.0.jar"

echo Todos los microservicios han sido lanzados.
echo Swagger UI disponible en:
echo   Fleet Management : http://localhost:8081/swagger-ui.html
echo   GPS Ingestion    : http://localhost:8082/swagger-ui.html
echo   Route Calculation: http://localhost:8083/swagger-ui.html
echo   Alert Service    : http://localhost:8084/swagger-ui.html
pause
