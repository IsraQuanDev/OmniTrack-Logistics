@echo off
echo Deteniendo microservicios en puertos 8081, 8082, 8083 y 8084...

for %%P in (8081 8082 8083 8084) do (
    for /f "tokens=5" %%a in ('netstat -aon ^| findstr :%%P') do (
        taskkill /F /PID %%a 2>nul
    )
)

echo Microservicios detenidos.
pause
