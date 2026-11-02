import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule],
  template: `
    <aside class="app-sidebar">
      <nav class="sidebar-nav">
        <button 
          class="nav-item" 
          [class.active]="activeTab === 'map'"
          (click)="selectTab('map')">
          <span class="icon">🗺️</span>
          <span class="label">Mapa en Vivo</span>
          <span class="badge active-badge">LIVE</span>
        </button>

        <button 
          class="nav-item" 
          [class.active]="activeTab === 'route'"
          (click)="selectTab('route')">
          <span class="icon">📐</span>
          <span class="label">Rutas & Geocercas</span>
        </button>

        <button 
          class="nav-item" 
          [class.active]="activeTab === 'fleet'"
          (click)="selectTab('fleet')">
          <span class="icon">🚚</span>
          <span class="label">Gestión de Flota</span>
        </button>

        <button 
          class="nav-item" 
          [class.active]="activeTab === 'alerts'"
          (click)="selectTab('alerts')">
          <span class="icon">🚨</span>
          <span class="label">Centro de Alertas</span>
          <span class="badge alert-badge">3</span>
        </button>

        <button 
          class="nav-item" 
          [class.active]="activeTab === 'api'"
          (click)="selectTab('api')">
          <span class="icon">⚡</span>
          <span class="label">Consola Endpoints</span>
          <span class="badge api-badge">REST</span>
        </button>
      </nav>

      <div class="sidebar-footer">
        <div class="system-meta">
          <div class="meta-row">
            <span>Spring WebFlux:</span>
            <strong class="green">3.4.1</strong>
          </div>
          <div class="meta-row">
            <span>Throughput:</span>
            <strong>14,850 p/s</strong>
          </div>
          <div class="meta-row">
            <span>Latencia:</span>
            <strong class="cyan">3.2 ms</strong>
          </div>
        </div>
      </div>
    </aside>
  `,
  styles: [`
    .app-sidebar {
      width: 240px;
      background: #0b1329;
      border-right: 1px solid #1e293b;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      height: 100%;
      box-sizing: border-box;
    }
    .sidebar-nav {
      padding: 16px 10px;
      display: flex;
      flex-direction: column;
      gap: 6px;
    }
    .nav-item {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 11px 14px;
      background: transparent;
      border: 1px solid transparent;
      border-radius: 8px;
      color: #94a3b8;
      font-size: 0.88rem;
      font-weight: 500;
      cursor: pointer;
      text-align: left;
      transition: all 0.2s ease;
      font-family: inherit;
      width: 100%;
    }
    .nav-item:hover {
      background: rgba(30, 41, 59, 0.7);
      color: #f1f5f9;
    }
    .nav-item.active {
      background: rgba(2, 132, 199, 0.15);
      border-color: rgba(2, 132, 199, 0.4);
      color: #38bdf8;
      font-weight: 600;
      box-shadow: 0 0 12px rgba(2, 132, 199, 0.1);
    }
    .nav-item .icon {
      font-size: 1.1rem;
    }
    .nav-item .label {
      flex: 1;
    }
    .badge {
      font-size: 0.65rem;
      font-weight: 700;
      padding: 2px 6px;
      border-radius: 4px;
    }
    .active-badge {
      background: rgba(34, 197, 94, 0.2);
      color: #22c55e;
    }
    .alert-badge {
      background: #ef4444;
      color: #fff;
    }
    .api-badge {
      background: rgba(56, 189, 248, 0.2);
      color: #38bdf8;
    }
    .sidebar-footer {
      padding: 16px;
      border-top: 1px solid #1e293b;
      background: #090e1f;
    }
    .system-meta {
      display: flex;
      flex-direction: column;
      gap: 6px;
      font-size: 0.75rem;
      color: #64748b;
    }
    .meta-row {
      display: flex;
      justify-content: space-between;
    }
    .meta-row strong {
      color: #cbd5e1;
    }
    .green { color: #22c55e !important; }
    .cyan { color: #38bdf8 !important; }
  `]
})
export class SidebarComponent {
  @Input() activeTab = 'map';
  @Output() tabChanged = new EventEmitter<string>();

  selectTab(tab: string): void {
    this.tabChanged.emit(tab);
  }
}
