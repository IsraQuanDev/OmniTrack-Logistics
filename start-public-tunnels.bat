@echo off
echo ========================================================
echo Iniciando Tuneles Publicos Cloudflare para OmniTrack
echo ========================================================

start "Tunnel Fleet (8081)" "C:\Program Files (x86)\cloudflared\cloudflared.exe" tunnel --url http://localhost:8081
start "Tunnel GPS (8082)" "C:\Program Files (x86)\cloudflared\cloudflared.exe" tunnel --url http://localhost:8082
start "Tunnel Route (8083)" "C:\Program Files (x86)\cloudflared\cloudflared.exe" tunnel --url http://localhost:8083
start "Tunnel Alert (8084)" "C:\Program Files (x86)\cloudflared\cloudflared.exe" tunnel --url http://localhost:8084

echo Los 4 tuneles han sido iniciados.
echo Revisa las consolas abiertas para ver las URLs publicas HTTPS asignadas.
pause
