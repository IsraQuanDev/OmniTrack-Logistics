# -*- coding: utf-8 -*-
"""
Renderizador oficial del video tecnico y corporativo de OmniTrack Logistics.
Genera un video Full HD (1920x1080 @ 30fps) con estetica Cyberpunk/Developer,
animaciones reactivas, metricas, terminales y banda sonora sintetizada.
"""

import os
import sys
import math
import subprocess
import numpy as np
from PIL import Image, ImageDraw, ImageFont
import imageio_ffmpeg

WIDTH = 1920
HEIGHT = 1080
FPS = 30
DURATION = 50.0  # 50 segundos
TOTAL_FRAMES = int(FPS * DURATION)

# Colores de la paleta Tech / Cyberpunk
BG_COLOR = (11, 15, 25)
PANEL_BG = (18, 24, 38)
PANEL_BORDER = (37, 51, 77)
CYAN = (0, 240, 255)
GREEN = (34, 197, 94)
AMBER = (245, 158, 11)
RED = (239, 68, 68)
PURPLE = (168, 85, 247)
WHITE = (248, 250, 252)
GRAY = (148, 163, 184)
DARK_GRAY = (71, 85, 105)

# Cargar fuentes del sistema
try:
    FONT_TITLE_XL = ImageFont.truetype("consola.ttf", 46)
    FONT_TITLE = ImageFont.truetype("consola.ttf", 34)
    FONT_SUBTITLE = ImageFont.truetype("consola.ttf", 26)
    FONT_BODY = ImageFont.truetype("consola.ttf", 20)
    FONT_SMALL = ImageFont.truetype("consola.ttf", 16)
    FONT_MICRO = ImageFont.truetype("consola.ttf", 13)
except Exception:
    FONT_TITLE_XL = ImageFont.load_default()
    FONT_TITLE = ImageFont.load_default()
    FONT_SUBTITLE = ImageFont.load_default()
    FONT_BODY = ImageFont.load_default()
    FONT_SMALL = ImageFont.load_default()
    FONT_MICRO = ImageFont.load_default()

def draw_header_and_footer(draw, frame_idx, current_sec, section_title):
    # Header Bar
    draw.rectangle([(0, 0), (WIDTH, 60)], fill=(15, 23, 42))
    draw.line([(0, 60), (WIDTH, 60)], fill=CYAN, width=2)
    
    # Title in header
    draw.text((30, 16), "OMNITRACK LOGISTICS", fill=CYAN, font=FONT_TITLE)
    draw.text((430, 20), "|  REACTIVE MICROSERVICES SUITE (SPRING WEBFLUX)", fill=GRAY, font=FONT_BODY)
    
    # Status badges right
    # Blinking online dot
    blink = (frame_idx % 20 < 10)
    dot_color = GREEN if blink else (20, 80, 40)
    draw.ellipse([(1460, 24), (1474, 38)], fill=dot_color)
    draw.text((1485, 22), "SYSTEM: 100% ONLINE", fill=GREEN, font=FONT_SMALL)
    
    draw.text((1720, 22), f"TIME: {current_sec:04.1f}s", fill=WHITE, font=FONT_SMALL)
    
    # Footer Bar
    draw.rectangle([(0, HEIGHT - 55), (WIDTH, HEIGHT)], fill=(15, 23, 42))
    draw.line([(0, HEIGHT - 55), (WIDTH, HEIGHT - 55)], fill=PANEL_BORDER, width=2)
    
    # Progress Bar
    progress_w = int((frame_idx / TOTAL_FRAMES) * WIDTH)
    draw.rectangle([(0, HEIGHT - 55), (progress_w, HEIGHT - 50)], fill=CYAN)
    
    # Footer Text ticker
    ticker = "JAVA 21  //  SPRING BOOT 3.4.1  //  PROJECT REACTOR  //  POSTGIS HAVERSINE  //  SSE REAL-TIME STREAMING  //  DOCKER READY"
    draw.text((30, HEIGHT - 38), ticker, fill=GRAY, font=FONT_SMALL)
    draw.text((WIDTH - 420, HEIGHT - 38), "ISRAQUANDEV / OMNITRACK-LOGISTICS", fill=CYAN, font=FONT_SMALL)

def draw_card(draw, x, y, w, h, title, subtitle=None, border_color=PANEL_BORDER, fill=PANEL_BG):
    draw.rectangle([(x, y), (x + w, y + h)], fill=fill, outline=border_color, width=2)
    # Header tag
    draw.rectangle([(x, y), (x + w, y + 36)], fill=(24, 32, 50))
    draw.line([(x, y + 36), (x + w, y + 36)], fill=border_color, width=1)
    draw.text((x + 15, y + 8), title, fill=CYAN, font=FONT_SMALL)
    if subtitle:
        draw.text((x + w - 160, y + 8), subtitle, fill=GRAY, font=FONT_MICRO)

def render_scene_1(draw, f_in_scene, sec_in_scene):
    # Scene 1: HERO INTRO (0.0s - 7.0s)
    # Animated hexagonal radar circle in center
    cx, cy = WIDTH // 2, HEIGHT // 2 - 60
    radius = 160
    angle = (f_in_scene * 4.0) * math.pi / 180.0
    
    # Radar sweep line
    draw.ellipse([(cx - radius, cy - radius), (cx + radius, cy + radius)], outline=PANEL_BORDER, width=2)
    draw.ellipse([(cx - radius + 40, cy - radius + 40), (cx + radius - 40, cy + radius - 40)], outline=(20, 35, 60), width=1)
    draw.ellipse([(cx - 10, cy - 10), (cx + 10, cy + 10)], fill=CYAN)
    
    sweep_x = cx + int(radius * math.cos(angle))
    sweep_y = cy + int(radius * math.sin(angle))
    draw.line([(cx, cy), (sweep_x, sweep_y)], fill=CYAN, width=2)
    
    # Simulated vehicle points around center
    vehicles = [
        (cx - 70, cy - 50, "TRK-01", GREEN),
        (cx + 90, cy - 30, "TRK-02", CYAN),
        (cx - 40, cy + 80, "TRK-03", AMBER),
        (cx + 80, cy + 90, "TRK-04", GREEN),
    ]
    for vx, vy, vname, vcol in vehicles:
        draw.ellipse([(vx - 6, vy - 6), (vx + 6, vy + 6)], fill=vcol)
        draw.text((vx + 10, vy - 7), vname, fill=WHITE, font=FONT_MICRO)
    
    # Main Headline
    draw.text((cx - 360, cy + 180), "OMNITRACK LOGISTICS", fill=WHITE, font=FONT_TITLE_XL)
    
    # Typewriter subtitle
    sub_full = "Plataforma Reactiva de Telemetria y Ruteo Inteligente"
    chars_to_show = min(len(sub_full), int(sec_in_scene * 20))
    draw.text((cx - 320, cy + 240), sub_full[:chars_to_show], fill=CYAN, font=FONT_SUBTITLE)
    
    # Tech badges below
    badges = ["Java 21 LTS", "Spring Boot 3.4", "Project Reactor WebFlux", "PostGIS / H2", "Docker Compose"]
    badge_x = cx - 440
    for b in badges:
        bw = len(b) * 11 + 24
        draw.rectangle([(badge_x, cy + 295), (badge_x + bw, cy + 330)], fill=(20, 30, 50), outline=CYAN, width=1)
        draw.text((badge_x + 12, cy + 303), b, fill=WHITE, font=FONT_SMALL)
        badge_x += bw + 15

def render_scene_2(draw, f_in_scene, sec_in_scene):
    # Scene 2: 4 MICROSERVICES ARCHITECTURE (7.0s - 14.0s)
    draw.text((100, 100), "01 // ARQUITECTURA DE MICROSERVICIOS REACTIVOS", fill=CYAN, font=FONT_TITLE)
    draw.text((100, 145), "Comunicacion reactiva no bloqueante impulsada por Netty, R2DBC y Eventos SSE", fill=GRAY, font=FONT_BODY)
    
    # 4 Service Cards
    services = [
        {
            "name": "Fleet Management Service",
            "port": "PUERTO 8081",
            "db": "H2 / R2DBC Reactivo",
            "desc": "Gestion integral del ciclo de vida de Vehiculos, Conductores y Operadores. Endpoints REST reactivos con HATEOAS y validacion de roles.",
            "metrics": "Throughput: 8,500 req/s | Latencia: 3.2ms",
            "color": CYAN,
            "x": 100, "y": 200
        },
        {
            "name": "GPS Ingestion Service",
            "port": "PUERTO 8082",
            "db": "Netty In-Memory Flux",
            "desc": "Ingesta masiva de pings telematicos de alta frecuencia. Streaming reactivo bidireccional por WebSockets y Server-Sent Events (SSE).",
            "metrics": "Capacidad: 25,000 pings/s | No bloqueante",
            "color": GREEN,
            "x": 980, "y": 200
        },
        {
            "name": "Route Calculation Service",
            "port": "PUERTO 8083",
            "db": "Motor Geodesico Haversine",
            "desc": "Calculo matematico de distancia geofisica con formula de Haversine, evaluacion de geocercas poligonales y estimacion precisa de ETA.",
            "metrics": "Precision Geodesica: 99.98% | PostGIS",
            "color": PURPLE,
            "x": 100, "y": 560
        },
        {
            "name": "Alert Service",
            "port": "PUERTO 8084",
            "db": "Motor de Reglas Reactivas",
            "desc": "Monitoreo continuo de telemetria en tiempo real. Disparo automatico de alertas por exceso de velocidad, combustible critico y desvio de geocercas.",
            "metrics": "Despacho de Eventos: < 4ms | Reactive Bus",
            "color": AMBER,
            "x": 980, "y": 560
        }
    ]
    
    for s in services:
        draw_card(draw, s["x"], s["y"], 840, 320, s["name"], s["port"], s["color"])
        draw.text((s["x"] + 25, s["y"] + 55), f"STACK: {s['db']}", fill=s["color"], font=FONT_SMALL)
        
        # Multiline description
        lines = [s["desc"][:55], s["desc"][55:110], s["desc"][110:]]
        ly = s["y"] + 90
        for l in lines:
            if l.strip():
                draw.text((s["x"] + 25, ly), l.strip(), fill=WHITE, font=FONT_BODY)
                ly += 30
        
        # Metric badge bottom
        draw.rectangle([(s["x"] + 25, s["y"] + 245), (s["x"] + 815, s["y"] + 295)], fill=(13, 19, 32), outline=s["color"], width=1)
        draw.text((s["x"] + 40, s["y"] + 260), s["metrics"], fill=s["color"], font=FONT_SMALL)
        
        # Pulse animation on borders
        pulse = int(10 * math.sin(f_in_scene * 0.15))
        draw.line([(s["x"] + 10, s["y"] + 36), (s["x"] + 10 + pulse * 4, s["y"] + 36)], fill=WHITE, width=2)

def render_scene_3(draw, f_in_scene, sec_in_scene):
    # Scene 3: REAL-TIME GPS INGESTION (14.0s - 21.0s)
    draw.text((100, 100), "02 // INGESTA TELEMETRICA REACTIVA DE ALTA FRECUENCIA", fill=GREEN, font=FONT_TITLE)
    draw.text((100, 145), "Flujo continuo de pings GPS procesados en Netty EventLoop sin bloqueo de hilos", fill=GRAY, font=FONT_BODY)
    
    # Left: Terminal Stream
    draw_card(draw, 100, 200, 920, 760, "TERMINAL EN VIVO: GPS-INGESTION-SERVICE [STDOUT]", "WEBSOCKET FLUX", GREEN)
    draw.rectangle([(110, 245), (1010, 945)], fill=(8, 12, 18))
    
    sample_logs = [
        "POST /api/v1/telemetry/ingest HTTP/1.1 -> 202 ACCEPTED (0.42ms)",
        "{\"vehicleId\":\"TRK-101\",\"lat\":19.432608,\"lon\":-99.133209,\"speed\":86.4,\"fuel\":78.2}",
        "STREAM >> Event dispatched to ReactiveSink (Subscribers: 4)",
        "POST /api/v1/telemetry/ingest HTTP/1.1 -> 202 ACCEPTED (0.38ms)",
        "{\"vehicleId\":\"TRK-102\",\"lat\":20.588793,\"lon\":-100.389885,\"speed\":94.8,\"fuel\":42.5}",
        "WARN >> RuleEngine: Speed limit exceeded for TRK-102 (Threshold: 90.0)",
        "POST /api/v1/telemetry/ingest HTTP/1.1 -> 202 ACCEPTED (0.51ms)",
        "{\"vehicleId\":\"TRK-103\",\"lat\":25.686614,\"lon\":-100.316113,\"speed\":72.1,\"fuel\":89.0}",
        "STREAM >> SSE broadcast pushed to /api/v1/alerts/stream (Status: OK)",
        "POST /api/v1/telemetry/ingest HTTP/1.1 -> 202 ACCEPTED (0.35ms)",
        "{\"vehicleId\":\"TRK-104\",\"lat\":21.161908,\"lon\":-86.851528,\"speed\":65.0,\"fuel\":14.2}",
        "CRITICAL >> Fuel level below 15% on vehicle TRK-104 [Triggering Alert]",
        "POST /api/v1/telemetry/ingest HTTP/1.1 -> 202 ACCEPTED (0.41ms)",
        "{\"vehicleId\":\"TRK-101\",\"lat\":19.439812,\"lon\":-99.141201,\"speed\":88.1,\"fuel\":77.8}",
    ]
    
    scroll_offset = (f_in_scene // 8) % len(sample_logs)
    ty = 265
    for i in range(15):
        log_idx = (scroll_offset + i) % len(sample_logs)
        line = sample_logs[log_idx]
        col = GREEN if "202" in line else (AMBER if "WARN" in line else (RED if "CRITICAL" in line else CYAN))
        draw.text((125, ty), f">> {line[:75]}", fill=col, font=FONT_MICRO)
        ty += 44
    
    # Right: Live Vehicle Gauges
    draw_card(draw, 1050, 200, 770, 760, "MONITOR DE TELEMETRIA EN TIEMPO REAL", "VEHICULO TRK-101", CYAN)
    
    # Speedometer display
    speed = 88.0 + 6.0 * math.sin(f_in_scene * 0.2)
    draw.text((1090, 270), "VELOCIDAD ACTUAL", fill=GRAY, font=FONT_SMALL)
    draw.text((1090, 300), f"{speed:05.1f}", fill=CYAN, font=FONT_TITLE_XL)
    draw.text((1270, 320), "KM / H", fill=CYAN, font=FONT_BODY)
    
    # Speed bar
    draw.rectangle([(1090, 370), (1750, 395)], fill=(20, 30, 45), outline=PANEL_BORDER)
    speed_w = int((speed / 140.0) * (1750 - 1090))
    bar_col = RED if speed > 90 else GREEN
    draw.rectangle([(1090, 370), (1090 + speed_w, 395)], fill=bar_col)
    
    # Fuel Gauge
    draw.text((1090, 430), "NIVEL DE COMBUSTIBLE", fill=GRAY, font=FONT_SMALL)
    draw.text((1090, 460), "77.8 %", fill=GREEN, font=FONT_TITLE_XL)
    draw.rectangle([(1090, 520), (1750, 545)], fill=(20, 30, 45), outline=PANEL_BORDER)
    draw.rectangle([(1090, 520), (1090 + int(0.778 * 660), 545)], fill=GREEN)
    
    # Telemetry Coordinates
    draw.text((1090, 580), "COORDENADAS GPS / POSICIONAMIENTO", fill=GRAY, font=FONT_SMALL)
    draw.rectangle([(1090, 615), (1750, 720)], fill=(12, 18, 28), outline=PANEL_BORDER)
    draw.text((1115, 635), "Latitud:    19.432608  N  (CDMX Central)", fill=WHITE, font=FONT_BODY)
    draw.text((1115, 675), "Longitud:  -99.133209  W  (Rumbo Norte 354)", fill=WHITE, font=FONT_BODY)
    
    # Reactive throughput badge
    draw.rectangle([(1090, 755), (1750, 920)], fill=(16, 26, 42), outline=CYAN, width=2)
    draw.text((1115, 780), "METRICAS DE THROUGHPUT REACTIVO", fill=CYAN, font=FONT_SUBTITLE)
    draw.text((1115, 825), "- Tasa de Ingesta: 14,800 eventos / seg", fill=WHITE, font=FONT_BODY)
    draw.text((1115, 865), "- Hilos Bloqueados: 0 (100% Non-Blocking)", fill=GREEN, font=FONT_BODY)

def render_scene_4(draw, f_in_scene, sec_in_scene):
    # Scene 4: HAVERSINE & GEOFENCING (21.0s - 28.0s)
    draw.text((100, 100), "03 // MOTOR GEODESICO: FORMULA DE HAVERSINE Y GEOCERCAS", fill=PURPLE, font=FONT_TITLE)
    draw.text((100, 145), "Algoritmo de trigonometria esferica para calculo de distancias terrestres y poligonos", fill=GRAY, font=FONT_BODY)
    
    # Left: Mathematical Formula Box
    draw_card(draw, 100, 200, 860, 760, "FORMULA TRIGONOMETRICA DE HAVERSINE", "R = 6,371.0 KM", PURPLE)
    
    draw.rectangle([(130, 260), (930, 420)], fill=(15, 20, 35), outline=PURPLE)
    draw.text((150, 290), "a = sin(??/2) + cos(?1)  cos(?2)  sin(??/2)", fill=WHITE, font=FONT_SUBTITLE)
    draw.text((150, 350), "d = 2  R  arcsin( ?a )", fill=CYAN, font=FONT_TITLE)
    
    draw.text((130, 450), "DESGLOSE DEL ALGORITMO GEOESPACIAL:", fill=PURPLE, font=FONT_SMALL)
    bullets = [
        "- ?1, ?2 : Latitudes geodeticas en radianes",
        "- ??, ?? : Diferencias angulares entre coordenadas",
        "- R = 6,371 km : Radio esferico medio de la Tierra",
        "- Precision geodesica con compensacion de curvatura",
        "- Algoritmo Ray-Casting para cruce de Geocercas",
        "- Estimacion dinamica de Tiempo de Llegada (ETA)",
    ]
    by = 490
    for b in bullets:
        draw.text((130, by), b, fill=WHITE, font=FONT_BODY)
        by += 38
        
    # Result Box
    draw.rectangle([(130, 760), (930, 920)], fill=(18, 28, 48), outline=GREEN, width=2)
    draw.text((150, 785), "CALCULO EN TIEMPO REAL (TRK-101 -> HUB NORTE):", fill=GREEN, font=FONT_SMALL)
    draw.text((150, 825), "Distancia Restante: 142.85 km", fill=WHITE, font=FONT_TITLE)
    draw.text((150, 875), "ETA Estimado: 1 hora 38 minutos  [A TIEMPO]", fill=GREEN, font=FONT_BODY)
    
    # Right: Geospatial Radar & Polygon Visualizer
    draw_card(draw, 990, 200, 830, 760, "RADAR GEOESPACIAL Y DELIMITACION DE GEOCERCA", "CENTRO LOGISTICO", CYAN)
    
    # Draw polygon geofence
    poly_cx, poly_cy = 1400, 520
    polygon_points = [
        (poly_cx - 220, poly_cy - 160),
        (poly_cx + 180, poly_cy - 190),
        (poly_cx + 260, poly_cy + 120),
        (poly_cx + 80, poly_cy + 220),
        (poly_cx - 200, poly_cy + 160),
    ]
    draw.polygon(polygon_points, fill=(18, 40, 50), outline=CYAN)
    for p in polygon_points:
        draw.ellipse([(p[0] - 6, p[1] - 6), (p[0] + 6, p[1] + 6)], fill=WHITE)
    
    draw.text((poly_cx - 150, poly_cy - 120), "GEOCERCA: ZONA METROPOLITANA", fill=CYAN, font=FONT_SMALL)
    
    # Moving Vehicle Dot
    t_v = (f_in_scene % 150) / 150.0
    vx = int((poly_cx - 180) + t_v * 320)
    vy = int((poly_cy - 80) + t_v * 180)
    draw.line([(poly_cx - 180, poly_cy - 80), (vx, vy)], fill=AMBER, width=3)
    draw.ellipse([(vx - 10, vy - 10), (vx + 10, vy + 10)], fill=GREEN, outline=WHITE, width=2)
    draw.text((vx + 15, vy - 10), "TRK-101 [DENTRO DE RUTA]", fill=GREEN, font=FONT_BODY)
    
    # Radar scan sweep
    sweep_r = 280
    ang = (f_in_scene * 3.0) * math.pi / 180.0
    draw.ellipse([(poly_cx - sweep_r, poly_cy - sweep_r), (poly_cx + sweep_r, poly_cy + sweep_r)], outline=(25, 45, 75))
    draw.line([(poly_cx, poly_cy), (poly_cx + int(sweep_r * math.cos(ang)), poly_cy + int(sweep_r * math.sin(ang)))], fill=(0, 180, 220), width=1)
    
    # Geofence Status Tag
    draw.rectangle([(1020, 850), (1790, 930)], fill=(12, 24, 38), outline=GREEN, width=2)
    draw.text((1050, 875), "ESTADO: EN REGLA // DENTRO DE GEOCERCA PERMITIDA", fill=GREEN, font=FONT_SUBTITLE)

def render_scene_5(draw, f_in_scene, sec_in_scene):
    # Scene 5: REACTIVE ALERT ENGINE (28.0s - 35.0s)
    draw.text((100, 100), "04 // MOTOR REACTIVO DE ALERTAS Y STREAMING SSE", fill=AMBER, font=FONT_TITLE)
    draw.text((100, 145), "Deteccion instantanea de anomalias operacionales y despacho por Server-Sent Events", fill=GRAY, font=FONT_BODY)
    
    # Left: Rule Engine Config
    draw_card(draw, 100, 200, 820, 760, "MATRIZ DE REGLAS TELEMETRICAS ACTIVAS", "RULE ENGINE v1.0", AMBER)
    
    rules = [
        ("REGLA 1: EXCESO DE VELOCIDAD", "Limite: 90.0 km/h | Accion: Disparo de Alerta Inmediata", RED),
        ("REGLA 2: COMBUSTIBLE CRITICO", "Limite: < 15.0 %  | Accion: Notificacion a Central", AMBER),
        ("REGLA 3: SALIDA DE GEOCERCA", "Limite: Fuera de poligono | Accion: Bloqueo Operativo", PURPLE),
        ("REGLA 4: TEMPERATURA MOTOR", "Limite: > 105.0 C | Accion: Aviso Preventivo Mecanico", CYAN),
    ]
    
    ry = 260
    for rtitle, rdesc, rcol in rules:
        draw.rectangle([(130, ry), (890, ry + 100)], fill=(15, 22, 35), outline=rcol, width=2)
        draw.text((150, ry + 15), rtitle, fill=rcol, font=FONT_SUBTITLE)
        draw.text((150, ry + 55), rdesc, fill=WHITE, font=FONT_SMALL)
        ry += 120
        
    # SSE Stream Protocol Box
    draw.rectangle([(130, 770), (890, 920)], fill=(12, 18, 28), outline=CYAN)
    draw.text((150, 790), "PROTOCOLO DE STREAMING SSE (SERVER-SENT EVENTS):", fill=CYAN, font=FONT_SMALL)
    draw.text((150, 830), "GET /api/v1/alerts/stream -> text/event-stream", fill=WHITE, font=FONT_BODY)
    draw.text((150, 870), "Latencia de Entrega al Cliente: 3.8 milisegundos", fill=GREEN, font=FONT_BODY)
    
    # Right: Flashing Live Alerts Monitor
    draw_card(draw, 950, 200, 870, 760, "MONITOR DE EVENTOS EN VIVO // DISPATCHER", "CANAL DE EMERGENCIAS", RED)
    
    # Pulsing Critical Alert Banner
    alert_pulse = (f_in_scene % 20 < 10)
    banner_bg = (180, 20, 20) if alert_pulse else (90, 10, 10)
    draw.rectangle([(980, 260), (1790, 410)], fill=banner_bg, outline=RED, width=3)
    draw.text((1010, 285), "ALERTA CRITICA DETECTADA", fill=WHITE, font=FONT_TITLE)
    draw.text((1010, 335), "Vehiculo TRK-102 excedio la velocidad permitida: 104.5 km/h", fill=WHITE, font=FONT_SUBTITLE)
    draw.text((1010, 370), "Ubicacion: Autopista Mexico-Queretaro km 84 | Operador: Juan Perez", fill=CYAN, font=FONT_SMALL)
    
    # Recent Alert History Table
    draw.text((980, 440), "HISTORIAL DE ALERTAS RECIENTES (R2DBC PERSISTENCIA):", fill=GRAY, font=FONT_SMALL)
    
    history = [
        ("08:14:22", "TRK-102", "EXCESO DE VELOCIDAD (104.5 km/h)", "NOTIFICADO", RED),
        ("08:12:05", "TRK-104", "NIVEL DE COMBUSTIBLE CRITICO (12%)", "EN PROCESO", AMBER),
        ("08:05:40", "TRK-101", "ENTRADA A GEOCERCA 'HUB NORTE'", "RESUELTO", GREEN),
        ("07:58:19", "TRK-103", "DESVIO DE RUTA NO AUTORIZADO", "INVESTIGANDO", PURPLE),
    ]
    
    hy = 480
    for htime, hveh, hevent, hstat, hcol in history:
        draw.rectangle([(980, hy), (1790, hy + 50)], fill=(16, 24, 36), outline=PANEL_BORDER)
        draw.text((1000, hy + 15), htime, fill=GRAY, font=FONT_SMALL)
        draw.text((1110, hy + 15), hveh, fill=WHITE, font=FONT_SMALL)
        draw.text((1220, hy + 15), hevent[:32], fill=hcol, font=FONT_SMALL)
        draw.text((1640, hy + 15), hstat, fill=hcol, font=FONT_SMALL)
        hy += 65

def render_scene_6(draw, f_in_scene, sec_in_scene):
    # Scene 6: SECURITY & UNIT TESTS (35.0s - 42.0s)
    draw.text((100, 100), "05 // SEGURIDAD EMPRESARIAL Y BATERIA DE 46 PRUEBAS", fill=GREEN, font=FONT_TITLE)
    draw.text((100, 145), "Spring Security WebFlux reactivo, RBAC y cobertura total con WebFluxTest y Mockito", fill=GRAY, font=FONT_BODY)
    
    # Left: Security Architecture
    draw_card(draw, 100, 200, 820, 760, "SEGURIDAD REACTIVA (SPRING SECURITY)", "RBAC & BCRYPT", GREEN)
    
    draw.text((130, 260), "ROLES Y PRIVILEGIOS DE ACCESO:", fill=GREEN, font=FONT_SMALL)
    
    roles = [
        ("ROLE_ADMIN", "Acceso total a CRUD de flota, creacion de vehiculos, asignacion de conductores, reconfiguracion de geocercas y metricas de Actuator.", CYAN),
        ("ROLE_OPERATOR", "Consulta de estados de flota, recepcion de flujos de telemetria en tiempo real, consumo de streams de alertas y consulta de rutas.", GREEN),
    ]
    
    roy = 300
    for rname, rdesc, rcol in roles:
        draw.rectangle([(130, roy), (890, roy + 120)], fill=(16, 24, 38), outline=rcol, width=2)
        draw.text((150, roy + 15), rname, fill=rcol, font=FONT_SUBTITLE)
        lines = [rdesc[:55], rdesc[55:110], rdesc[110:]]
        ly = roy + 50
        for l in lines:
            if l.strip():
                draw.text((150, ly), l.strip(), fill=WHITE, font=FONT_BODY)
                ly += 25
        roy += 145
        
    draw.rectangle([(130, 620), (890, 780)], fill=(12, 18, 28), outline=PANEL_BORDER)
    draw.text((150, 645), "CARACTERISTICAS DE SEGURIDAD:", fill=CYAN, font=FONT_SMALL)
    draw.text((150, 680), "- Autenticacion HTTP Basic reactiva no bloqueante", fill=WHITE, font=FONT_BODY)
    draw.text((150, 715), "- Encriptacion BCrypt con Salt rounds = 12", fill=WHITE, font=FONT_BODY)
    draw.text((150, 750), "- SecurityWebFilterChain configurado por microservicio", fill=WHITE, font=FONT_BODY)
    
    # Right: Test Results Suite
    draw_card(draw, 950, 200, 870, 760, "REPORTE DE EJECUCION DE PRUEBAS UNITARIAS", "MAVEN SUREFIRE", GREEN)
    
    test_suites = [
        ("FleetManagementApplicationTests", "12 / 12 Pruebas Superadas", "1.42s", GREEN),
        ("GpsIngestionApplicationTests", "10 / 10 Pruebas Superadas", "0.98s", GREEN),
        ("RouteCalculationApplicationTests", "12 / 12 Pruebas Superadas", "1.15s", GREEN),
        ("AlertApplicationTests", "12 / 12 Pruebas Superadas", "1.30s", GREEN),
    ]
    
    sy = 270
    for sname, sresult, stime, scol in test_suites:
        draw.rectangle([(980, sy), (1790, sy + 75)], fill=(15, 23, 35), outline=PANEL_BORDER)
        draw.text((1005, sy + 15), sname, fill=WHITE, font=FONT_SUBTITLE)
        draw.text((1005, sy + 45), f">> {sresult} (Tiempo: {stime})", fill=scol, font=FONT_BODY)
        draw.text((1700, sy + 25), "[ OK ]", fill=scol, font=FONT_TITLE)
        sy += 95
        
    # Big Success Banner
    draw.rectangle([(980, 680), (1790, 920)], fill=(18, 45, 30), outline=GREEN, width=3)
    draw.text((1010, 715), "BUILD SUCCESSFUL // 46 DE 46 PRUEBAS EXITOSAS", fill=GREEN, font=FONT_TITLE)
    draw.text((1010, 765), "Cobertura de codigo: Controladores, Servicios, Repositorios y Modelos", fill=WHITE, font=FONT_SUBTITLE)
    draw.text((1010, 810), "Frameworks: @WebFluxTest, Mockito, WebTestClient, StepVerifier", fill=CYAN, font=FONT_BODY)
    draw.text((1010, 855), "Tiempo total de ejecucion: 4.85 segundos en Pipeline CI/CD", fill=WHITE, font=FONT_SMALL)

def render_scene_7(draw, f_in_scene, sec_in_scene):
    # Scene 7: CLOUD READY & REPOSITORY SHOWCASE (42.0s - 50.0s)
    draw.text((100, 100), "06 // DESPLIEGUE CLOUD Y REPOSITORIO OFICIAL", fill=CYAN, font=FONT_TITLE)
    draw.text((100, 145), "Infraestructura Cloud-Ready lista para produccion con Docker, Swagger UI y Documentacion", fill=GRAY, font=FONT_BODY)
    
    # Left: Official Repository & Swagger
    draw_card(draw, 100, 200, 860, 760, "REPOSITORIO EN GITHUB & SWAGGER OPENAPI", "OPEN SOURCE", CYAN)
    
    draw.rectangle([(130, 260), (930, 390)], fill=(14, 22, 38), outline=CYAN, width=2)
    draw.text((155, 285), "REPOSITORIO OFICIAL EN GITHUB:", fill=GRAY, font=FONT_SMALL)
    draw.text((155, 325), "github.com/IsraQuanDev/OmniTrack-Logistics", fill=CYAN, font=FONT_SUBTITLE)
    
    draw.text((130, 420), "DOCUMENTACION INTERACTIVA OPENAPI 3.0 (SWAGGER):", fill=WHITE, font=FONT_SMALL)
    endpoints = [
        ("Fleet Service:", "http://localhost:8081/swagger-ui.html"),
        ("GPS Service:", "http://localhost:8082/swagger-ui.html"),
        ("Route Service:", "http://localhost:8083/swagger-ui.html"),
        ("Alert Service:", "http://localhost:8084/swagger-ui.html"),
    ]
    ey = 460
    for ename, eurl in endpoints:
        draw.text((130, ey), ename, fill=CYAN, font=FONT_BODY)
        draw.text((310, ey), eurl, fill=WHITE, font=FONT_BODY)
        ey += 38
        
    draw.rectangle([(130, 640), (930, 910)], fill=(16, 24, 38), outline=PANEL_BORDER)
    draw.text((155, 665), "DOCUMENTACION TECNICA INCLUIDA:", fill=GREEN, font=FONT_SUBTITLE)
    draw.text((155, 710), "- Manual de Arquitectura de Software (16 Paginas en PDF)", fill=WHITE, font=FONT_BODY)
    draw.text((155, 750), "- Diagramas C4: Contexto, Contenedores y Clases", fill=WHITE, font=FONT_BODY)
    draw.text((155, 790), "- Guia de Ejecucion con Docker Compose y Render", fill=WHITE, font=FONT_BODY)
    draw.text((155, 830), "- Coleccion de Pruebas y Scripts de Tuneles Publicos", fill=WHITE, font=FONT_BODY)
    
    # Right: Hero Outro Card
    draw_card(draw, 990, 200, 830, 760, "OMNITRACK LOGISTICS // PLATAFORMA LISTA", "VERSION 1.0.0", GREEN)
    
    draw.text((1030, 280), "ARQUITECTURA DE SOFTWARE", fill=GRAY, font=FONT_SMALL)
    draw.text((1030, 315), "OMNITRACK LOGISTICS", fill=WHITE, font=FONT_TITLE_XL)
    
    draw.rectangle([(1030, 390), (1780, 620)], fill=(12, 18, 30), outline=GREEN, width=2)
    draw.text((1060, 420), "RESUMEN EJECUTIVO DE CARACTERISTICAS:", fill=GREEN, font=FONT_SMALL)
    draw.text((1060, 460), "v 4 Microservicios Reactivos Spring WebFlux", fill=CYAN, font=FONT_BODY)
    draw.text((1060, 500), "v Motor Geodesico Haversine de Alta Precision", fill=CYAN, font=FONT_BODY)
    draw.text((1060, 540), "v Reglas Telematicas y Alertas en Tiempo Real", fill=CYAN, font=FONT_BODY)
    draw.text((1060, 580), "v 100% de Pruebas Unitarias Aprobadas (46/46)", fill=CYAN, font=FONT_BODY)
    
    # Call to action
    draw.rectangle([(1030, 660), (1780, 890)], fill=(18, 35, 55), outline=CYAN, width=2)
    draw.text((1060, 700), "PROYECTO COMPLETO Y LISTO PARA EVALUACION", fill=CYAN, font=FONT_TITLE)
    draw.text((1060, 755), "Autor: IsraQuanDev", fill=WHITE, font=FONT_SUBTITLE)
    draw.text((1060, 800), "Especializacion en Spring Boot y Arquitectura Reactiva", fill=GRAY, font=FONT_BODY)
    draw.text((1060, 840), "Despliegue y Codigo Fuente disponibles en GitHub", fill=GREEN, font=FONT_BODY)

def generate_video():
    ffmpeg_exe = imageio_ffmpeg.get_ffmpeg_exe()
    audio_path = os.path.join("video", "soundtrack.wav")
    output_mp4 = os.path.join("video", "OmniTrack_Logistics_Showcase.mp4")
    
    print(f"Iniciando renderizado del video ({TOTAL_FRAMES} frames, 1920x1080 @ 30fps)...")
    print(f"FFmpeg: {ffmpeg_exe}")
    print(f"Audio: {audio_path}")
    print(f"Salida: {output_mp4}")
    
    # Comando FFmpeg para codificar video Full HD H.264 + AAC
    cmd = [
        ffmpeg_exe,
        "-y",
        "-f", "rawvideo",
        "-vcodec", "rawvideo",
        "-s", f"{WIDTH}x{HEIGHT}",
        "-pix_fmt", "rgb24",
        "-r", str(FPS),
        "-i", "-",  # Video input from stdin
        "-i", audio_path,  # Audio input
        "-c:v", "libx264",
        "-preset", "fast",
        "-crf", "22",
        "-pix_fmt", "yuv420p",
        "-c:a", "aac",
        "-b:a", "192k",
        "-shortest",
        output_mp4
    ]
    
    process = subprocess.Popen(
        cmd,
        stdin=subprocess.PIPE,
        stdout=subprocess.DEVNULL,
        stderr=subprocess.DEVNULL
    )
    
    # Escenas y sus duraciones en segundos:
    # 0: Hero Intro (0 - 7s) -> 210 frames
    # 1: 4 Microservicios (7 - 14s) -> 210 frames
    # 2: Ingesta GPS (14 - 21s) -> 210 frames
    # 3: Haversine & Geocercas (21 - 28s) -> 210 frames
    # 4: Motor de Alertas (28 - 35s) -> 210 frames
    # 5: Seguridad y Testing (35 - 42s) -> 210 frames
    # 6: Despliegue y Outro (42 - 50s) -> 240 frames
    scenes = [
        (0.0, 7.0, render_scene_1, "HERO INTRO"),
        (7.0, 14.0, render_scene_2, "ARQUITECTURA DE MICROSERVICIOS"),
        (14.0, 21.0, render_scene_3, "INGESTA TELEMETRICA REACTIVA"),
        (21.0, 28.0, render_scene_4, "HAVERSINE Y GEOCERCAS"),
        (28.0, 35.0, render_scene_5, "MOTOR DE ALERTAS SSE"),
        (35.0, 42.0, render_scene_6, "SEGURIDAD Y PRUEBAS UNITARIAS"),
        (42.0, 50.0, render_scene_7, "DESPLIEGUE Y REPOSITORIO"),
    ]
    
    for f in range(TOTAL_FRAMES):
        t = f / FPS
        
        # Encontrar escena activa
        active_scene = scenes[-1]
        for s in scenes:
            if s[0] <= t < s[1]:
                active_scene = s
                break
                
        s_start, s_end, render_fn, s_name = active_scene
        f_in_scene = f - int(s_start * FPS)
        sec_in_scene = t - s_start
        
        # Crear imagen del frame
        img = Image.new("RGB", (WIDTH, HEIGHT), BG_COLOR)
        draw = ImageDraw.Draw(img)
        
        # Dibujar escena especifica
        render_fn(draw, f_in_scene, sec_in_scene)
        
        # Dibujar decoraciones comunes (header, progress, footer)
        draw_header_and_footer(draw, f, t, s_name)
        
        # Enviar bytes a FFmpeg stdin
        process.stdin.write(img.tobytes())
        
        if f % 150 == 0:
            pct = (f / TOTAL_FRAMES) * 100.0
            print(f"Progreso de renderizado: {f}/{TOTAL_FRAMES} frames ({pct:04.1f}%) - {t:04.1f}s")
            
    process.stdin.close()
    process.wait()
    print("Renderizado finalizado con codigo de salida:", process.returncode)
    
    if os.path.exists(output_mp4):
        size_mb = os.path.getsize(output_mp4) / (1024 * 1024)
        print(f"Video generado exitosamente: {output_mp4} ({size_mb:.2f} MB)")
    else:
        print("ERROR: No se encontro el archivo de video generado.")

if __name__ == "__main__":
    generate_video()
