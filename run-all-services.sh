#!/bin/bash
# ========================================================
# OmniTrack Logistics - Iniciar los 4 Microservicios (Linux/macOS)
# ========================================================

echo "Iniciando OmniTrack Logistics..."

java -jar fleet-management-service/target/fleet-management-service-1.0.0.jar > fleet.log 2>&1 &
echo "Fleet Management Service iniciado en PID $! (Puerto 8081)"

java -jar gps-ingestion-service/target/gps-ingestion-service-1.0.0.jar > gps.log 2>&1 &
echo "GPS Ingestion Service iniciado en PID $! (Puerto 8082)"

java -jar route-calculation-service/target/route-calculation-service-1.0.0.jar > route.log 2>&1 &
echo "Route Calculation Service iniciado en PID $! (Puerto 8083)"

java -jar alert-service/target/alert-service-1.0.0.jar > alert.log 2>&1 &
echo "Alert Service iniciado en PID $! (Puerto 8084)"

echo ""
echo "Todos los servicios se están ejecutando en segundo plano."
echo "Swagger UI disponible en:"
echo "  Fleet Management : http://localhost:8081/swagger-ui.html"
echo "  GPS Ingestion    : http://localhost:8082/swagger-ui.html"
echo "  Route Calculation: http://localhost:8083/swagger-ui.html"
echo "  Alert Service    : http://localhost:8084/swagger-ui.html"
