import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FleetService } from '../../services/fleet.service';
import { RouteService } from '../../services/route.service';
import { Vehicle } from '../../models/omnitrack.models';

@Component({
  selector: 'app-live-map',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="map-view-container">
      <!-- Top Metric Cards -->
      <div class="metrics-grid">
        <div class="metric-card">
          <div class="metric-label">FLOTA ACTIVA</div>
          <div class="metric-value">48 / 52</div>
          <div class="metric-sub green">92.3% Operando en tiempo real</div>
        </div>

        <div class="metric-card">
          <div class="metric-label">INGESTA TELEMÉTRICA</div>
          <div class="metric-value">14,850 <small>pings/s</small></div>
          <div class="metric-sub cyan">Netty WebFlux Non-Blocking EventLoop</div>
        </div>

        <div class="metric-card">
          <div class="metric-label">ALERTAS CRÍTICAS</div>
          <div class="metric-value red">3 ACTIVAS</div>
          <div class="metric-sub">2 Velocidad, 1 Combustible bajo</div>
        </div>

        <div class="metric-card">
          <div class="metric-label">CUMPLIMIENTO ETA</div>
          <div class="metric-value">97.2%</div>
          <div class="metric-sub green">Optimizado por motor Haversine</div>
        </div>
      </div>

      <!-- Map & Telemetry Split Screen -->
      <div class="map-main-wrapper">
        <div class="map-canvas-box">
          <div class="map-hud-overlay">
            <span class="hud-tag">MAPA SATELITAL DE FLOTA // COBERTURA MÉXICO - ESTADOS UNIDOS</span>
            <div class="map-controls">
              <button class="map-btn" (click)="toggleLayer('traffic')">🚦 Tráfico</button>
              <button class="map-btn" (click)="toggleLayer('geofence')">🛡️ Geocercas</button>
              <button class="map-btn active">📡 Live GPS</button>
            </div>
          </div>

          <!-- Interactive Radar Simulation SVG -->
          <svg class="map-svg-viewport" viewBox="0 0 1000 620">
            <!-- Background Map Grid -->
            <defs>
              <pattern id="grid" width="40" height="40" patternUnits="userSpaceOnUse">
                <path d="M 40 0 L 0 0 0 40" fill="none" stroke="#162035" stroke-width="1"/>
              </pattern>
              <linearGradient id="routeGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" stop-color="#0284c7" stop-opacity="0.8"/>
                <stop offset="100%" stop-color="#38bdf8" stop-opacity="1"/>
              </linearGradient>
            </defs>
            <rect width="100%" height="100%" fill="#0a0f1d"/>
            <rect width="100%" height="100%" fill="url(#grid)"/>

            <!-- Geofence Zone 1 (Central Hub Circle) -->
            <circle cx="500" cy="380" r="70" fill="rgba(34, 197, 94, 0.12)" stroke="#22c55e" stroke-width="2" stroke-dasharray="6,4"/>
            <text x="445" y="385" fill="#22c55e" font-size="11" font-family="monospace">HUB CDMX (SEGURO)</text>

            <!-- Geofence Zone 2 (Transit Corridor Polygon) -->
            <polygon points="480,360 450,260 490,240 540,340" fill="rgba(245, 158, 11, 0.15)" stroke="#f59e0b" stroke-width="2"/>
            <text x="440" y="300" fill="#f59e0b" font-size="10" font-family="monospace">CORREDOR MX-57D</text>

            <!-- Geofence Zone 3 (Restricted Danger Zone) -->
            <polygon points="320,290 380,270 370,340 310,330" fill="rgba(239, 68, 68, 0.18)" stroke="#ef4444" stroke-width="2"/>
            <text x="325" y="315" fill="#ef4444" font-size="10" font-family="monospace">ZONA RESTRINGIDA</text>

            <!-- Highway Route Polyline (CDMX - Querétaro - San Luis - Monterrey) -->
            <polyline 
              points="500,380 470,310 460,250 510,140 530,90" 
              fill="none" 
              stroke="url(#routeGradient)" 
              stroke-width="4" 
              stroke-linecap="round"/>

            <!-- Highway Route Polyline (CDMX - Guadalajara - Manzanillo) -->
            <polyline 
              points="500,380 390,360 300,370 230,420" 
              fill="none" 
              stroke="#06b6d4" 
              stroke-width="3" 
              stroke-dasharray="8,5"/>

            <!-- Vehicle Markers -->
            <!-- TRK-101 -->
            <g class="truck-marker" (click)="selectVehicle(vehicles[0])" transform="translate(470, 310)">
              <circle r="18" fill="rgba(56, 189, 248, 0.25)" class="pulse-ring"/>
              <circle r="8" fill="#38bdf8" stroke="#fff" stroke-width="2"/>
              <text x="14" y="4" fill="#fff" font-size="11" font-family="monospace" font-weight="bold">TRK-101 (86 km/h)</text>
            </g>

            <!-- TRK-102 (Speeding - Red alert) -->
            <g class="truck-marker" (click)="selectVehicle(vehicles[1])" transform="translate(460, 250)">
              <circle r="22" fill="rgba(239, 68, 68, 0.3)" class="pulse-ring-alert"/>
              <circle r="9" fill="#ef4444" stroke="#fff" stroke-width="2"/>
              <text x="14" y="4" fill="#ef4444" font-size="11" font-family="monospace" font-weight="bold">TRK-102 (95 km/h ⚠️)</text>
            </g>

            <!-- TRK-103 -->
            <g class="truck-marker" (click)="selectVehicle(vehicles[2])" transform="translate(530, 90)">
              <circle r="16" fill="rgba(34, 197, 94, 0.25)"/>
              <circle r="8" fill="#22c55e" stroke="#fff" stroke-width="2"/>
              <text x="14" y="4" fill="#22c55e" font-size="11" font-family="monospace" font-weight="bold">TRK-103 (72 km/h)</text>
            </g>

            <!-- TRK-104 (Low Fuel - Amber) -->
            <g class="truck-marker" (click)="selectVehicle(vehicles[3])" transform="translate(300, 370)">
              <circle r="18" fill="rgba(245, 158, 11, 0.3)"/>
              <circle r="8" fill="#f59e0b" stroke="#fff" stroke-width="2"/>
              <text x="14" y="4" fill="#f59e0b" font-size="11" font-family="monospace" font-weight="bold">TRK-104 (12% ⛽)</text>
            </g>
          </svg>
        </div>

        <!-- Right Telemetry Panel -->
        <div class="telemetry-drawer" *ngIf="selectedVehicle">
          <div class="drawer-header">
            <div>
              <h3>{{ selectedVehicle.id }}</h3>
              <span class="sub-model">{{ selectedVehicle.model }}</span>
            </div>
            <span class="status-badge" [class.active]="selectedVehicle.status === 'ACTIVE'">
              {{ selectedVehicle.status }}
            </span>
          </div>

          <div class="telemetry-gauges">
            <div class="gauge-box">
              <span class="gauge-title">VELOCIDAD</span>
              <span class="gauge-number" [class.speeding]="selectedVehicle.speed > 90">
                {{ selectedVehicle.speed }} <small>km/h</small>
              </span>
              <div class="gauge-bar">
                <div class="gauge-fill" [style.width.%]="(selectedVehicle.speed / 140) * 100"></div>
              </div>
            </div>

            <div class="gauge-box">
              <span class="gauge-title">COMBUSTIBLE</span>
              <span class="gauge-number" [class.low-fuel]="selectedVehicle.fuelLevel < 15">
                {{ selectedVehicle.fuelLevel }} <small>%</small>
              </span>
              <div class="gauge-bar">
                <div class="gauge-fill fuel" [style.width.%]="selectedVehicle.fuelLevel"></div>
              </div>
            </div>
          </div>

          <div class="details-list">
            <div class="detail-row">
              <span class="label">Conductor Asignado:</span>
              <span class="val">{{ selectedVehicle.driverName }}</span>
            </div>
            <div class="detail-row">
              <span class="label">Placas / Registro:</span>
              <span class="val">{{ selectedVehicle.plate }}</span>
            </div>
            <div class="detail-row">
              <span class="label">Coordenadas GPS:</span>
              <span class="val cyan">{{ selectedVehicle.latitude }}, {{ selectedVehicle.longitude }}</span>
            </div>
            <div class="detail-row">
              <span class="label">Temperatura Motor:</span>
              <span class="val">{{ selectedVehicle.engineTemperature }} °C</span>
            </div>
            <div class="detail-row">
              <span class="label">Ruta Actual:</span>
              <span class="val">{{ selectedVehicle.currentRoute }}</span>
            </div>
          </div>

          <div class="drawer-actions">
            <button class="action-btn primary">📡 Solicitar Ping Inmediato</button>
            <button class="action-btn danger">🚨 Disparar Alerta a Operador</button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .map-view-container {
      display: flex;
      flex-direction: column;
      gap: 16px;
      height: 100%;
      box-sizing: border-box;
    }
    .metrics-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 16px;
    }
    .metric-card {
      background: #0f172a;
      border: 1px solid #1e293b;
      border-radius: 10px;
      padding: 16px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.3);
    }
    .metric-label {
      font-size: 0.72rem;
      color: #94a3b8;
      text-transform: uppercase;
      letter-spacing: 0.5px;
      margin-bottom: 4px;
    }
    .metric-value {
      font-size: 1.5rem;
      font-weight: 700;
      color: #f8fafc;
    }
    .metric-value small {
      font-size: 0.8rem;
      color: #94a3b8;
    }
    .metric-sub {
      font-size: 0.75rem;
      margin-top: 4px;
      color: #64748b;
    }
    .green { color: #22c55e !important; }
    .cyan { color: #38bdf8 !important; }
    .red { color: #ef4444 !important; }

    .map-main-wrapper {
      display: flex;
      gap: 16px;
      flex: 1;
      min-height: 480px;
    }
    .map-canvas-box {
      flex: 1;
      background: #0a0f1d;
      border: 1px solid #1e293b;
      border-radius: 12px;
      position: relative;
      overflow: hidden;
      display: flex;
      flex-direction: column;
    }
    .map-hud-overlay {
      position: absolute;
      top: 12px;
      left: 16px;
      right: 16px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      z-index: 10;
      pointer-events: none;
    }
    .hud-tag {
      font-size: 0.75rem;
      font-family: monospace;
      color: #38bdf8;
      background: rgba(15, 23, 42, 0.85);
      padding: 4px 10px;
      border-radius: 6px;
      border: 1px solid #1e293b;
    }
    .map-controls {
      pointer-events: auto;
      display: flex;
      gap: 8px;
    }
    .map-btn {
      background: rgba(15, 23, 42, 0.85);
      border: 1px solid #334155;
      color: #94a3b8;
      padding: 6px 12px;
      border-radius: 6px;
      font-size: 0.75rem;
      cursor: pointer;
    }
    .map-btn.active {
      background: #0284c7;
      color: #fff;
      border-color: #38bdf8;
    }
    .map-svg-viewport {
      width: 100%;
      height: 100%;
      flex: 1;
    }
    .truck-marker {
      cursor: pointer;
      transition: transform 0.2s;
    }
    .truck-marker:hover {
      transform: scale(1.15);
    }
    .pulse-ring {
      animation: pulse 2s infinite;
    }
    .pulse-ring-alert {
      animation: pulseAlert 1s infinite;
    }
    @keyframes pulse {
      0% { transform: scale(0.8); opacity: 0.8; }
      100% { transform: scale(1.6); opacity: 0; }
    }
    @keyframes pulseAlert {
      0% { transform: scale(0.8); opacity: 1; }
      100% { transform: scale(1.8); opacity: 0; }
    }

    .telemetry-drawer {
      width: 320px;
      background: #0f172a;
      border: 1px solid #1e293b;
      border-radius: 12px;
      padding: 20px;
      display: flex;
      flex-direction: column;
      gap: 16px;
    }
    .drawer-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      border-bottom: 1px solid #1e293b;
      padding-bottom: 12px;
    }
    .drawer-header h3 {
      margin: 0;
      color: #f8fafc;
      font-size: 1.2rem;
    }
    .sub-model {
      font-size: 0.75rem;
      color: #94a3b8;
    }
    .status-badge {
      font-size: 0.7rem;
      font-weight: 700;
      padding: 3px 8px;
      border-radius: 4px;
      background: rgba(34, 197, 94, 0.15);
      color: #22c55e;
    }
    .telemetry-gauges {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 12px;
    }
    .gauge-box {
      background: #1e293b;
      border-radius: 8px;
      padding: 10px;
      text-align: center;
    }
    .gauge-title {
      font-size: 0.68rem;
      color: #94a3b8;
      display: block;
    }
    .gauge-number {
      font-size: 1.25rem;
      font-weight: 700;
      color: #38bdf8;
      display: block;
      margin: 4px 0;
    }
    .speeding { color: #ef4444 !important; }
    .low-fuel { color: #f59e0b !important; }
    .gauge-bar {
      height: 6px;
      background: #334155;
      border-radius: 3px;
      overflow: hidden;
    }
    .gauge-fill {
      height: 100%;
      background: #38bdf8;
    }
    .gauge-fill.fuel {
      background: #22c55e;
    }
    .details-list {
      display: flex;
      flex-direction: column;
      gap: 10px;
      font-size: 0.8rem;
    }
    .detail-row {
      display: flex;
      justify-content: space-between;
      border-bottom: 1px dashed #1e293b;
      padding-bottom: 6px;
    }
    .detail-row .label { color: #94a3b8; }
    .detail-row .val { color: #f1f5f9; font-weight: 500; }
    .drawer-actions {
      margin-top: auto;
      display: flex;
      flex-direction: column;
      gap: 8px;
    }
    .action-btn {
      padding: 10px;
      border-radius: 6px;
      border: none;
      font-weight: 600;
      font-size: 0.8rem;
      cursor: pointer;
    }
    .action-btn.primary {
      background: #0284c7;
      color: #fff;
    }
    .action-btn.danger {
      background: rgba(239, 68, 68, 0.15);
      color: #ef4444;
      border: 1px solid rgba(239, 68, 68, 0.3);
    }
  `]
})
export class LiveMapComponent implements OnInit {
  vehicles: Vehicle[] = [];
  selectedVehicle: Vehicle | null = null;

  constructor(private fleetService: FleetService) {}

  ngOnInit(): void {
    this.vehicles = this.fleetService.vehicles();
    if (this.vehicles.length > 0) {
      this.selectedVehicle = this.vehicles[0];
    }
  }

  selectVehicle(v: Vehicle): void {
    this.selectedVehicle = v;
  }

  toggleLayer(layer: string): void {
    // layer toggle action
  }
}
