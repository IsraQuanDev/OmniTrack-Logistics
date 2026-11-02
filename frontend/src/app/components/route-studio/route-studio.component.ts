import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouteService } from '../../services/route.service';
import { RouteCalculationResponse, GeofenceZone } from '../../models/omnitrack.models';

@Component({
  selector: 'app-route-studio',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="studio-container">
      <div class="studio-left">
        <div class="panel-card">
          <h2 class="panel-title">📐 PLANIFICADOR DE RUTAS HAVERSINE</h2>
          <p class="panel-desc">Calcula distancia geodésica, tiempo estimado (ETA) y costo de peajes en base al radio esférico terrestre (R = 6,371 km).</p>

          <div class="form-group">
            <label>Punto de Origen (Lat, Lon):</label>
            <div class="coord-inputs">
              <input type="number" [(ngModel)]="originLat" placeholder="Latitud" step="0.0001" />
              <input type="number" [(ngModel)]="originLon" placeholder="Longitud" step="0.0001" />
            </div>
          </div>

          <div class="form-group">
            <label>Punto de Destino (Lat, Lon):</label>
            <div class="coord-inputs">
              <input type="number" [(ngModel)]="destLat" placeholder="Latitud" step="0.0001" />
              <input type="number" [(ngModel)]="destLon" placeholder="Longitud" step="0.0001" />
            </div>
          </div>

          <div class="presets-row">
            <button class="preset-pill" (click)="setPreset('CDMX-QRO')">CDMX -> Querétaro</button>
            <button class="preset-pill" (click)="setPreset('CDMX-MTY')">CDMX -> Monterrey</button>
            <button class="preset-pill" (click)="setPreset('CDMX-GDL')">CDMX -> Guadalajara</button>
          </div>

          <button class="calculate-btn" (click)="calculateRoute()">
            ⚡ Calcular con Motor Geodésico
          </button>
        </div>

        <!-- Calculation Result Box -->
        <div class="panel-card result-box" *ngIf="calcResult">
          <h3 class="result-title">RESULTADOS DEL CÁLCULO</h3>
          
          <div class="result-metric">
            <span class="label">Distancia Geodésica:</span>
            <span class="value cyan">{{ calcResult.distanceKm }} km</span>
          </div>

          <div class="result-metric">
            <span class="label">Tiempo Estimado (ETA):</span>
            <span class="value green">{{ formatMinutes(calcResult.estimatedTravelTimeMinutes) }}</span>
          </div>

          <div class="result-metric">
            <span class="label">Hora Prevista de Arribo:</span>
            <span class="value">{{ calcResult.estimatedArrivalIso | date:'short' }}</span>
          </div>

          <div class="result-metric">
            <span class="label">Peajes Estimados:</span>
            <span class="value">\${{ calcResult.tollCostEstimatedMxn }} MXN</span>
          </div>

          <div class="algorithm-badge">
            {{ calcResult.algorithmUsed }}
          </div>
        </div>
      </div>

      <!-- Right: Geofencing Zones Studio -->
      <div class="studio-right">
        <div class="panel-card">
          <div class="card-header-flex">
            <div>
              <h2 class="panel-title">🛡️ CATÁLOGO DE GEOCERCAS ACTIVAS</h2>
              <p class="panel-desc">Perímetros circulares y poligonales evaluados con algoritmo de Ray-Casting.</p>
            </div>
            <button class="add-geofence-btn">+ Nueva Geocerca</button>
          </div>

          <div class="geofence-list">
            <div class="geofence-item" *ngFor="let g of geofences" [class]="g.severity.toLowerCase()">
              <div class="geo-top">
                <span class="geo-name">{{ g.name }}</span>
                <span class="geo-type-pill">{{ g.type }}</span>
              </div>
              <div class="geo-meta">
                <span>Severidad: <strong [class]="g.severity.toLowerCase()">{{ g.severity }}</strong></span>
                <span>Vértices / Centro: {{ g.coordinates.length }} puntos</span>
                <span>Estado: {{ g.status }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .studio-container {
      display: grid;
      grid-template-columns: 460px 1fr;
      gap: 20px;
      height: 100%;
    }
    .panel-card {
      background: #0f172a;
      border: 1px solid #1e293b;
      border-radius: 12px;
      padding: 24px;
      box-shadow: 0 4px 16px rgba(0,0,0,0.3);
      display: flex;
      flex-direction: column;
      gap: 16px;
    }
    .panel-title {
      font-size: 1.05rem;
      color: #f8fafc;
      margin: 0;
    }
    .panel-desc {
      font-size: 0.8rem;
      color: #94a3b8;
      margin: 0;
      line-height: 1.4;
    }
    .form-group {
      display: flex;
      flex-direction: column;
      gap: 6px;
    }
    .form-group label {
      font-size: 0.8rem;
      color: #cbd5e1;
      font-weight: 500;
    }
    .coord-inputs {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 10px;
    }
    .coord-inputs input {
      background: #1e293b;
      border: 1px solid #334155;
      border-radius: 8px;
      padding: 8px 12px;
      color: #f8fafc;
      font-family: monospace;
      outline: none;
    }
    .presets-row {
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
    }
    .preset-pill {
      background: #1e293b;
      border: 1px solid #334155;
      color: #38bdf8;
      padding: 5px 10px;
      border-radius: 6px;
      font-size: 0.75rem;
      cursor: pointer;
    }
    .preset-pill:hover {
      background: #0284c7;
      color: #fff;
    }
    .calculate-btn {
      background: #0284c7;
      color: #fff;
      border: none;
      border-radius: 8px;
      padding: 12px;
      font-weight: 600;
      font-size: 0.9rem;
      cursor: pointer;
      box-shadow: 0 0 12px rgba(2, 132, 199, 0.4);
    }
    .result-box {
      margin-top: 16px;
      background: #09101f;
      border-color: #0284c7;
    }
    .result-title {
      font-size: 0.9rem;
      color: #38bdf8;
      margin: 0;
    }
    .result-metric {
      display: flex;
      justify-content: space-between;
      border-bottom: 1px solid #1e293b;
      padding-bottom: 6px;
      font-size: 0.85rem;
    }
    .result-metric .label { color: #94a3b8; }
    .result-metric .value { font-weight: 700; color: #f1f5f9; }
    .cyan { color: #38bdf8 !important; }
    .green { color: #22c55e !important; }
    .algorithm-badge {
      font-size: 0.72rem;
      color: #64748b;
      font-family: monospace;
      text-align: center;
      margin-top: 6px;
    }
    .card-header-flex {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .add-geofence-btn {
      background: #22c55e;
      color: #000;
      border: none;
      padding: 8px 14px;
      border-radius: 6px;
      font-weight: 700;
      font-size: 0.8rem;
      cursor: pointer;
    }
    .geofence-list {
      display: flex;
      flex-direction: column;
      gap: 12px;
    }
    .geofence-item {
      background: #1e293b;
      border-left: 4px solid #334155;
      border-radius: 8px;
      padding: 14px;
      display: flex;
      flex-direction: column;
      gap: 8px;
    }
    .geofence-item.safe { border-left-color: #22c55e; }
    .geofence-item.warning { border-left-color: #f59e0b; }
    .geofence-item.critical { border-left-color: #ef4444; }
    .geo-top {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .geo-name {
      font-weight: 600;
      color: #f8fafc;
      font-size: 0.9rem;
    }
    .geo-type-pill {
      font-size: 0.7rem;
      font-family: monospace;
      padding: 2px 8px;
      border-radius: 4px;
      background: #0f172a;
      color: #94a3b8;
    }
    .geo-meta {
      display: flex;
      gap: 20px;
      font-size: 0.75rem;
      color: #94a3b8;
    }
    .safe { color: #22c55e; }
    .warning { color: #f59e0b; }
    .critical { color: #ef4444; }
  `]
})
export class RouteStudioComponent {
  originLat = 19.4326;
  originLon = -99.1332;
  destLat = 20.5888;
  destLon = -100.3899;

  calcResult: RouteCalculationResponse | null = null;
  geofences: GeofenceZone[] = [];

  constructor(private routeService: RouteService) {
    this.geofences = this.routeService.activeGeofences();
    this.calculateRoute();
  }

  setPreset(preset: string): void {
    if (preset === 'CDMX-QRO') {
      this.originLat = 19.4326; this.originLon = -99.1332;
      this.destLat = 20.5888; this.destLon = -100.3899;
    } else if (preset === 'CDMX-MTY') {
      this.originLat = 19.4326; this.originLon = -99.1332;
      this.destLat = 25.6866; this.destLon = -100.3161;
    } else if (preset === 'CDMX-GDL') {
      this.originLat = 19.4326; this.originLon = -99.1332;
      this.destLat = 20.6736; this.destLon = -103.3440;
    }
    this.calculateRoute();
  }

  calculateRoute(): void {
    this.routeService.calculateRoute({
      originLat: this.originLat,
      originLon: this.originLon,
      destLat: this.destLat,
      destLon: this.destLon
    }).subscribe(res => {
      this.calcResult = res;
    });
  }

  formatMinutes(minutes: number): string {
    const h = Math.floor(minutes / 60);
    const m = minutes % 60;
    return `${h}h ${m}m`;
  }
}
