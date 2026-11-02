import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  template: `
    <header class="app-header">
      <div class="header-left">
        <div class="logo-box">OT</div>
        <div class="brand-info">
          <h1 class="brand-title">OMNITRACK LOGISTICS</h1>
          <span class="brand-badge">ENTERPRISE REACTIVE SUITE</span>
        </div>
      </div>

      <div class="header-center">
        <div class="search-bar">
          <span class="search-icon">🔍</span>
          <input type="text" placeholder="Buscar vehículo (TRK-101), conductor, placas..." />
        </div>
      </div>

      <div class="header-right">
        <div class="status-pill online">
          <span class="pulse-dot"></span>
          <span>NETTY WEBFLUX: ONLINE</span>
        </div>
        <div class="status-pill sse">
          <span>SSE: 30 FPS</span>
        </div>
        <div class="notification-bell">
          <span>🔔</span>
          <span class="badge">3</span>
        </div>
        <div class="user-profile">
          <div class="avatar">IQ</div>
          <div class="user-details">
            <span class="user-name">Israel Quan</span>
            <span class="user-role">Senior Fleet Admin</span>
          </div>
        </div>
      </div>
    </header>
  `,
  styles: [`
    .app-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 24px;
      background: #0f172a;
      border-bottom: 1px solid #1e293b;
      height: 64px;
      box-sizing: border-box;
    }
    .header-left {
      display: flex;
      align-items: center;
      gap: 12px;
    }
    .logo-box {
      width: 36px;
      height: 36px;
      background: #0284c7;
      color: #fff;
      font-weight: 800;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 0 12px rgba(2, 132, 199, 0.5);
    }
    .brand-title {
      font-size: 1.1rem;
      font-weight: 700;
      letter-spacing: 0.5px;
      color: #f8fafc;
      margin: 0;
    }
    .brand-badge {
      font-size: 0.65rem;
      color: #38bdf8;
      letter-spacing: 0.5px;
    }
    .header-center {
      flex: 1;
      max-width: 480px;
      margin: 0 24px;
    }
    .search-bar {
      display: flex;
      align-items: center;
      gap: 8px;
      background: #1e293b;
      border: 1px solid #334155;
      border-radius: 8px;
      padding: 6px 14px;
    }
    .search-bar input {
      background: transparent;
      border: none;
      color: #f8fafc;
      width: 100%;
      outline: none;
      font-size: 0.85rem;
      font-family: inherit;
    }
    .header-right {
      display: flex;
      align-items: center;
      gap: 16px;
    }
    .status-pill {
      font-size: 0.72rem;
      font-weight: 600;
      padding: 4px 10px;
      border-radius: 20px;
      display: flex;
      align-items: center;
      gap: 6px;
    }
    .status-pill.online {
      background: rgba(34, 197, 94, 0.1);
      color: #22c55e;
      border: 1px solid rgba(34, 197, 94, 0.3);
    }
    .status-pill.sse {
      background: rgba(14, 165, 233, 0.1);
      color: #38bdf8;
      border: 1px solid rgba(14, 165, 233, 0.3);
    }
    .pulse-dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      background: #22c55e;
      box-shadow: 0 0 8px #22c55e;
    }
    .notification-bell {
      position: relative;
      cursor: pointer;
      font-size: 1.1rem;
    }
    .notification-bell .badge {
      position: absolute;
      top: -4px;
      right: -6px;
      background: #ef4444;
      color: #fff;
      font-size: 0.65rem;
      font-weight: 700;
      padding: 1px 5px;
      border-radius: 10px;
    }
    .user-profile {
      display: flex;
      align-items: center;
      gap: 10px;
      border-left: 1px solid #334155;
      padding-left: 16px;
    }
    .avatar {
      width: 32px;
      height: 32px;
      background: #3b82f6;
      color: #fff;
      font-size: 0.8rem;
      font-weight: 700;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .user-details {
      display: flex;
      flex-direction: column;
    }
    .user-name {
      font-size: 0.82rem;
      font-weight: 600;
      color: #f1f5f9;
    }
    .user-role {
      font-size: 0.7rem;
      color: #94a3b8;
    }
  `]
})
export class HeaderComponent {}
