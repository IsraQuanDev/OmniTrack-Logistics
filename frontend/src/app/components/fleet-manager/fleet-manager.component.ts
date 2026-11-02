import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FleetService } from '../../services/fleet.service';
import { Vehicle } from '../../models/omnitrack.models';

@Component({
  selector: 'app-fleet-manager',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="fleet-container">
      <div class="panel-card">
        <div class="card-header">
          <div>
            <h2 class="panel-title">🚚 GESTIÓN DE FLOTA Y CONDUCTORES</h2>
            <p class="panel-desc">Inventario de unidades, conductores asignados y telemetría de rendimiento en tiempo real.</p>
          </div>
          <button class="add-btn" (click)="openModal()">+ Registrar Unidad</button>
        </div>

        <div class="table-filters">
          <input type="text" [(ngModel)]="searchTerm" placeholder="Filtrar por placa, ID o conductor..." class="filter-input" />
          <div class="filter-tags">
            <button class="tag-btn active">Todas ({{ vehicles.length }})</button>
            <button class="tag-btn">Activas</button>
            <button class="tag-btn">En Mantenimiento</button>
          </div>
        </div>

        <div class="table-wrapper">
          <table class="fleet-table">
            <thead>
              <tr>
                <th>ID Unidad</th>
                <th>Modelo</th>
                <th>Placas</th>
                <th>Conductor</th>
                <th>Estado</th>
                <th>Velocidad</th>
                <th>Combustible</th>
                <th>Ruta Asignada</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let v of filteredVehicles">
                <td><strong class="cyan">{{ v.id }}</strong></td>
                <td>{{ v.model }}</td>
                <td><span class="plate-badge">{{ v.plate }}</span></td>
                <td>{{ v.driverName }}</td>
                <td>
                  <span class="status-pill" [class]="v.status.toLowerCase()">
                    {{ v.status }}
                  </span>
                </td>
                <td>
                  <strong [class.speeding]="v.speed > 90">{{ v.speed }} km/h</strong>
                </td>
                <td>
                  <div class="fuel-cell">
                    <span>{{ v.fuelLevel }}%</span>
                    <div class="mini-bar">
                      <div class="mini-fill" [style.width.%]="v.fuelLevel" [class.low]="v.fuelLevel < 15"></div>
                    </div>
                  </div>
                </td>
                <td><span class="route-tag">{{ v.currentRoute }}</span></td>
                <td>
                  <div class="action-buttons">
                    <button class="icon-btn" title="Ver en mapa">📍</button>
                    <button class="icon-btn" title="Editar">✏️</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .fleet-container {
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
      height: 100%;
      box-sizing: border-box;
    }
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 1px solid #1e293b;
      padding-bottom: 14px;
    }
    .panel-title {
      font-size: 1.15rem;
      color: #f8fafc;
      margin: 0;
    }
    .panel-desc {
      font-size: 0.82rem;
      color: #94a3b8;
      margin: 4px 0 0;
    }
    .add-btn {
      background: #0284c7;
      color: #fff;
      border: none;
      padding: 10px 18px;
      border-radius: 8px;
      font-weight: 600;
      font-size: 0.85rem;
      cursor: pointer;
    }
    .table-filters {
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 16px;
    }
    .filter-input {
      background: #1e293b;
      border: 1px solid #334155;
      color: #f8fafc;
      padding: 8px 14px;
      border-radius: 6px;
      width: 320px;
      outline: none;
      font-size: 0.85rem;
    }
    .filter-tags {
      display: flex;
      gap: 8px;
    }
    .tag-btn {
      background: #1e293b;
      border: 1px solid #334155;
      color: #94a3b8;
      padding: 6px 12px;
      border-radius: 6px;
      font-size: 0.78rem;
      cursor: pointer;
    }
    .tag-btn.active {
      background: rgba(2, 132, 199, 0.2);
      border-color: #0284c7;
      color: #38bdf8;
    }
    .table-wrapper {
      flex: 1;
      overflow-y: auto;
    }
    .fleet-table {
      width: 100%;
      border-collapse: collapse;
      font-size: 0.85rem;
      text-align: left;
    }
    .fleet-table th {
      background: #1e293b;
      color: #94a3b8;
      padding: 12px 14px;
      font-weight: 600;
      border-bottom: 1px solid #334155;
    }
    .fleet-table td {
      padding: 12px 14px;
      border-bottom: 1px solid #1e293b;
      color: #cbd5e1;
    }
    .cyan { color: #38bdf8; font-family: monospace; }
    .plate-badge {
      font-family: monospace;
      background: #1e293b;
      padding: 3px 6px;
      border-radius: 4px;
      border: 1px solid #334155;
      font-size: 0.75rem;
    }
    .status-pill {
      font-size: 0.7rem;
      font-weight: 700;
      padding: 3px 8px;
      border-radius: 4px;
    }
    .status-pill.active { background: rgba(34, 197, 94, 0.15); color: #22c55e; }
    .status-pill.maintenance { background: rgba(245, 158, 11, 0.15); color: #f59e0b; }
    .speeding { color: #ef4444 !important; }
    .fuel-cell {
      display: flex;
      align-items: center;
      gap: 8px;
    }
    .mini-bar {
      width: 50px;
      height: 6px;
      background: #334155;
      border-radius: 3px;
      overflow: hidden;
    }
    .mini-fill {
      height: 100%;
      background: #22c55e;
    }
    .mini-fill.low { background: #ef4444; }
    .route-tag {
      font-size: 0.75rem;
      color: #94a3b8;
    }
    .action-buttons {
      display: flex;
      gap: 6px;
    }
    .icon-btn {
      background: transparent;
      border: 1px solid #334155;
      border-radius: 4px;
      padding: 4px 6px;
      cursor: pointer;
    }
  `]
})
export class FleetManagerComponent implements OnInit {
  vehicles: Vehicle[] = [];
  searchTerm = '';

  constructor(private fleetService: FleetService) {}

  ngOnInit(): void {
    this.vehicles = this.fleetService.vehicles();
  }

  get filteredVehicles(): Vehicle[] {
    if (!this.searchTerm.trim()) return this.vehicles;
    const term = this.searchTerm.toLowerCase();
    return this.vehicles.filter(v => 
      v.id.toLowerCase().includes(term) ||
      v.driverName.toLowerCase().includes(term) ||
      v.plate.toLowerCase().includes(term)
    );
  }

  openModal(): void {
    // modal trigger
  }
}
