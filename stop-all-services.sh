#!/bin/bash
# ========================================================
# OmniTrack Logistics - Detener Microservicios (Linux/macOS)
# ========================================================

echo "Deteniendo los microservicios en puertos 8081, 8082, 8083 y 8084..."
for port in 8081 8082 8083 8084; do
  pid=$(lsof -ti :$port)
  if [ -n "$pid" ]; then
    kill -9 $pid
    echo "Servicio en puerto $port (PID $pid) detenido."
  else
    echo "Ningún proceso escuchando en puerto $port."
  fi
done
echo "Todos los servicios han sido detenidos."
