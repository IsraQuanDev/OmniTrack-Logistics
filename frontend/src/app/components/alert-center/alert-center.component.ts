import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertService } from '../../services/alert.service';
import { AlertNotification } from '../../models/omnitrack.models';

@Component({
  selector: 'app-alert-center',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="alert-center-container">
      <div class="panel-card">
        <div class="center-header">
          <div>
            <h2 class="panel-title">🚨 CENTRO DE ALERTAS & DESPACHO TELEMÉTRICO</h2>
            <p class="panel-desc">Stream reactivo de eventos críticos Server-Sent Events (SSE) y despacho de incidencias.</p>
          </div>
          <div class="badge-group">
            <span class="live-pulse">● CANAL SSE ACTIVO</span>
          </div>
        </div>

        <div class="alerts-feed">
          <div class="alert-card" *ngFor="let a of alerts" [class]="a.severity.toLowerCase()" [class.acknowledged]="a.acknowledged">
            <div class="card-left-badge">
              <span class="sev-tag">{{ a.severity }}</span>
            </div>

            <div class="card-content">
              <div class="alert-meta-top">
                <span class="veh-id">{{ a.vehicleId }}</span>
                <span class="rule-name">{{ a.ruleTriggered }}</span>
                <span class="timestamp">{{ a.timestamp | date:'mediumTime' }}</span>
              </div>
              <p class="alert-msg">{{ a.message }}</p>
              <div class="coords-tag">
                📍 Coordenadas de Incidente: {{ a.latitude }}, {{ a.longitude }}
              </div>
            </div>

            <div class="card-actions">
              <button class="btn-ack" *ngIf="!a.acknowledged" (click)="ackAlert(a.id)">
                ✓ Reconocer
              </button>
              <span class="ack-status" *ngIf="a.acknowledged">
                ✓ Atendido
              </span>
              <button class="btn-radio">📻 Enlace Radio</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .alert-center-container {
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
      gap: 20px;
      height: 100%;
      box-sizing: border-box;
    }
    .center-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 1px solid #1e293b;
      padding-bottom: 16px;
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
    .live-pulse {
      background: rgba(34, 197, 94, 0.15);
      color: #22c55e;
      border: 1px solid rgba(34, 197, 94, 0.3);
      padding: 6px 14px;
      border-radius: 20px;
      font-size: 0.75rem;
      font-weight: 700;
      letter-spacing: 0.5px;
    }
    .alerts-feed {
      display: flex;
      flex-direction: column;
      gap: 12px;
      overflow-y: auto;
    }
    .alert-card {
      display: flex;
      align-items: center;
      gap: 16px;
      background: #1e293b;
      border-left: 5px solid #334155;
      border-radius: 8px;
      padding: 16px;
      transition: all 0.2s ease;
    }
    .alert-card.critical {
      border-left-color: #ef4444;
      background: rgba(239, 68, 68, 0.05);
    }
    .alert-card.warning {
      border-left-color: #f59e0b;
      background: rgba(245, 158, 11, 0.05);
    }
    .alert-card.info {
      border-left-color: #38bdf8;
      background: rgba(56, 189, 248, 0.05);
    }
    .alert-card.acknowledged {
      opacity: 0.6;
    }
    .card-left-badge .sev-tag {
      font-size: 0.7rem;
      font-weight: 800;
      padding: 4px 8px;
      border-radius: 4px;
      letter-spacing: 0.5px;
    }
    .critical .sev-tag { background: #ef4444; color: #fff; }
    .warning .sev-tag { background: #f59e0b; color: #000; }
    .info .sev-tag { background: #0284c7; color: #fff; }
    .card-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 4px;
    }
    .alert-meta-top {
      display: flex;
      gap: 12px;
      align-items: center;
      font-size: 0.8rem;
    }
    .veh-id {
      font-weight: 700;
      color: #f8fafc;
      font-family: monospace;
    }
    .rule-name {
      color: #94a3b8;
    }
    .timestamp {
      margin-left: auto;
      color: #64748b;
      font-family: monospace;
      font-size: 0.75rem;
    }
    .alert-msg {
      margin: 2px 0;
      font-size: 0.88rem;
      color: #f1f5f9;
      font-weight: 500;
    }
    .coords-tag {
      font-size: 0.72rem;
      color: #38bdf8;
      font-family: monospace;
    }
    .card-actions {
      display: flex;
      flex-direction: column;
      gap: 6px;
    }
    .btn-ack {
      background: #22c55e;
      color: #000;
      border: none;
      padding: 6px 12px;
      border-radius: 6px;
      font-weight: 700;
      font-size: 0.75rem;
      cursor: pointer;
    }
    .ack-status {
      font-size: 0.75rem;
      color: #22c55e;
      font-weight: 600;
      text-align: center;
    }
    .btn-radio {
      background: #1e293b;
      border: 1px solid #334155;
      color: #94a3b8;
      padding: 6px 12px;
      border-radius: 6px;
      font-size: 0.75rem;
      cursor: pointer;
    }
  `]
})
export class AlertCenterComponent {
  alerts: AlertNotification[] = [];

  constructor(private alertService: AlertService) {
    this.alerts = this.alertService.activeAlerts();
  }

  ackAlert(id: string): void {
    this.alertService.acknowledgeAlert(id);
    this.alerts = this.alertService.activeAlerts();
  }
}
