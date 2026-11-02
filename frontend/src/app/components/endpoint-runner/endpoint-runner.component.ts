import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiRunnerService, ApiExecutionResult } from '../../services/api-runner.service';
import { EndpointPreset } from '../../models/omnitrack.models';

@Component({
  selector: 'app-endpoint-runner',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="runner-container">
      <!-- Left: Catalog & Request Builder -->
      <div class="runner-left">
        <div class="panel-card">
          <div class="card-header">
            <h2 class="panel-title">⚡ CONSOLA DE ENDPOINTS REST & OPENAPI</h2>
            <span class="subtitle">Ejecuta peticiones directamente a los 4 microservicios con autenticación HTTP Basic reactiva.</span>
          </div>

          <div class="preset-selector">
            <label>Seleccionar Endpoint del Catálogo:</label>
            <select [(ngModel)]="selectedPresetIndex" (change)="loadPreset()">
              <option *ngFor="let p of presets; let i = index" [value]="i">
                [{{ p.method }}] {{ p.serviceName }} (Port {{ p.port }}) -> {{ p.endpoint }}
              </option>
            </select>
          </div>

          <div class="request-builder">
            <div class="http-bar">
              <select [(ngModel)]="currentMethod" class="method-select" [class]="currentMethod.toLowerCase()">
                <option value="GET">GET</option>
                <option value="POST">POST</option>
                <option value="PUT">PUT</option>
                <option value="DELETE">DELETE</option>
              </select>
              <input type="text" [(ngModel)]="currentUrl" class="url-input" />
              <button class="send-btn" (click)="sendRequest()" [disabled]="isLoading">
                {{ isLoading ? 'Enviando...' : '🚀 Enviar' }}
              </button>
            </div>

            <div class="auth-bar">
              <span class="auth-label">HTTP Basic Auth:</span>
              <input type="text" [(ngModel)]="authUser" placeholder="Usuario" />
              <input type="password" [(ngModel)]="authPass" placeholder="Contraseña" />
            </div>

            <div class="payload-box" *ngIf="currentMethod === 'POST' || currentMethod === 'PUT'">
              <label>Cuerpo de la Petición (JSON Payload):</label>
              <textarea [(ngModel)]="currentPayload" rows="7" spellcheck="false"></textarea>
            </div>
          </div>
        </div>

        <!-- Microservices Health Grid -->
        <div class="health-grid">
          <div class="service-health-card">
            <div class="s-top">
              <strong>Fleet Service</strong>
              <span class="port-tag">:8081</span>
            </div>
            <span class="s-status online">● Online (2.8 ms)</span>
            <span class="s-desc">R2DBC H2 / CRUD</span>
          </div>

          <div class="service-health-card">
            <div class="s-top">
              <strong>GPS Service</strong>
              <span class="port-tag">:8082</span>
            </div>
            <span class="s-status online">● Online (1.1 ms)</span>
            <span class="s-desc">Netty Flux / SSE</span>
          </div>

          <div class="service-health-card">
            <div class="s-top">
              <strong>Route Service</strong>
              <span class="port-tag">:8083</span>
            </div>
            <span class="s-status online">● Online (3.9 ms)</span>
            <span class="s-desc">Haversine PostGIS</span>
          </div>

          <div class="service-health-card">
            <div class="s-top">
              <strong>Alert Service</strong>
              <span class="port-tag">:8084</span>
            </div>
            <span class="s-status online">● Online (2.4 ms)</span>
            <span class="s-desc">Reactive Rules SSE</span>
          </div>
        </div>
      </div>

      <!-- Right: Response Inspector -->
      <div class="runner-right">
        <div class="panel-card response-panel">
          <div class="response-header" *ngIf="executionResult">
            <div>
              <span class="status-code" [class.success]="!executionResult.isError" [class.error]="executionResult.isError">
                {{ executionResult.status }} {{ executionResult.statusText }}
              </span>
              <span class="time-tag">⏱️ {{ executionResult.durationMs }} ms</span>
            </div>
            <span class="timestamp-tag">{{ executionResult.timestamp | date:'mediumTime' }}</span>
          </div>

          <div class="no-response" *ngIf="!executionResult">
            <span>Ejecuta una petición desde la consola izquierda para inspeccionar la respuesta JSON y cabeceras en tiempo real.</span>
          </div>

          <div class="response-body" *ngIf="executionResult">
            <pre>{{ executionResult.data | json }}</pre>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .runner-container {
      display: grid;
      grid-template-columns: 580px 1fr;
      gap: 20px;
      height: 100%;
    }
    .runner-left {
      display: flex;
      flex-direction: column;
      gap: 16px;
    }
    .panel-card {
      background: #0f172a;
      border: 1px solid #1e293b;
      border-radius: 12px;
      padding: 20px;
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
    .subtitle {
      font-size: 0.8rem;
      color: #94a3b8;
    }
    .preset-selector {
      display: flex;
      flex-direction: column;
      gap: 6px;
    }
    .preset-selector label {
      font-size: 0.78rem;
      color: #cbd5e1;
    }
    .preset-selector select {
      background: #1e293b;
      border: 1px solid #334155;
      color: #f8fafc;
      padding: 8px 12px;
      border-radius: 8px;
      font-size: 0.82rem;
      outline: none;
    }
    .request-builder {
      display: flex;
      flex-direction: column;
      gap: 12px;
    }
    .http-bar {
      display: flex;
      gap: 8px;
    }
    .method-select {
      background: #1e293b;
      border: 1px solid #334155;
      color: #fff;
      font-weight: 700;
      padding: 8px 12px;
      border-radius: 6px;
      outline: none;
      font-family: monospace;
    }
    .method-select.get { color: #22c55e; }
    .method-select.post { color: #38bdf8; }
    .method-select.put { color: #f59e0b; }
    .method-select.delete { color: #ef4444; }
    .url-input {
      flex: 1;
      background: #1e293b;
      border: 1px solid #334155;
      color: #f8fafc;
      padding: 8px 12px;
      border-radius: 6px;
      font-family: monospace;
      font-size: 0.85rem;
      outline: none;
    }
    .send-btn {
      background: #0284c7;
      color: #fff;
      border: none;
      border-radius: 6px;
      padding: 0 18px;
      font-weight: 600;
      cursor: pointer;
    }
    .send-btn:hover { background: #0369a1; }
    .auth-bar {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 0.8rem;
    }
    .auth-label { color: #94a3b8; }
    .auth-bar input {
      background: #1e293b;
      border: 1px solid #334155;
      color: #f8fafc;
      padding: 5px 10px;
      border-radius: 6px;
      font-size: 0.8rem;
      outline: none;
    }
    .payload-box {
      display: flex;
      flex-direction: column;
      gap: 6px;
    }
    .payload-box label {
      font-size: 0.78rem;
      color: #cbd5e1;
    }
    .payload-box textarea {
      background: #090f1d;
      border: 1px solid #334155;
      color: #38bdf8;
      border-radius: 6px;
      padding: 10px;
      font-family: monospace;
      font-size: 0.8rem;
      outline: none;
      resize: vertical;
    }
    .health-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 10px;
    }
    .service-health-card {
      background: #0f172a;
      border: 1px solid #1e293b;
      border-radius: 8px;
      padding: 12px;
      display: flex;
      flex-direction: column;
      gap: 4px;
    }
    .s-top {
      display: flex;
      justify-content: space-between;
      font-size: 0.8rem;
      color: #f1f5f9;
    }
    .port-tag {
      font-size: 0.7rem;
      color: #38bdf8;
      font-family: monospace;
    }
    .s-status {
      font-size: 0.72rem;
      font-weight: 600;
    }
    .s-status.online { color: #22c55e; }
    .s-desc {
      font-size: 0.68rem;
      color: #64748b;
    }
    .response-panel {
      height: 100%;
      background: #090f1d;
      border-color: #1e293b;
    }
    .response-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 1px solid #1e293b;
      padding-bottom: 12px;
    }
    .status-code {
      font-weight: 700;
      padding: 4px 10px;
      border-radius: 6px;
      font-size: 0.85rem;
      font-family: monospace;
    }
    .status-code.success { background: rgba(34, 197, 94, 0.15); color: #22c55e; }
    .status-code.error { background: rgba(239, 68, 68, 0.15); color: #ef4444; }
    .time-tag {
      margin-left: 10px;
      font-size: 0.8rem;
      color: #94a3b8;
    }
    .timestamp-tag {
      font-size: 0.75rem;
      color: #64748b;
      font-family: monospace;
    }
    .no-response {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 300px;
      color: #64748b;
      font-size: 0.85rem;
      text-align: center;
      padding: 40px;
    }
    .response-body {
      flex: 1;
      overflow-y: auto;
    }
    .response-body pre {
      margin: 0;
      color: #38bdf8;
      font-family: 'Consolas', monospace;
      font-size: 0.85rem;
      line-height: 1.5;
    }
  `]
})
export class EndpointRunnerComponent {
  presets: EndpointPreset[] = [];
  selectedPresetIndex = 0;

  currentMethod: 'GET' | 'POST' | 'PUT' | 'DELETE' = 'GET';
  currentUrl = 'http://localhost:8081/api/v1/fleet/vehicles';
  currentPayload = '';
  authUser = 'admin';
  authPass = 'admin123';

  isLoading = false;
  executionResult: ApiExecutionResult | null = null;

  constructor(private apiRunner: ApiRunnerService) {
    this.presets = this.apiRunner.presets;
    this.loadPreset();
  }

  loadPreset(): void {
    const p = this.presets[this.selectedPresetIndex];
    if (p) {
      this.currentMethod = p.method;
      this.currentUrl = `http://localhost:${p.port}${p.endpoint}`;
      this.currentPayload = p.samplePayload || '';
    }
  }

  sendRequest(): void {
    this.isLoading = true;
    this.apiRunner.executeRequest(
      this.currentMethod,
      this.currentUrl,
      this.currentPayload,
      this.authUser,
      this.authPass
    ).subscribe(res => {
      this.executionResult = res;
      this.isLoading = false;
    });
  }
}
