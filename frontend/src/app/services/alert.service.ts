import { Injectable, signal } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { AlertNotification } from '../models/omnitrack.models';

@Injectable({
  providedIn: 'root'
})
export class AlertService {
  private readonly baseUrl = 'http://localhost:8084/api/v1/alerts';
  private eventSource: EventSource | null = null;

  public readonly activeAlerts = signal<AlertNotification[]>([
    {
      id: 'ALT-901',
      vehicleId: 'TRK-102',
      severity: 'CRITICAL',
      ruleTriggered: 'SPEED_LIMIT_EXCEEDED (>90 km/h)',
      message: 'Unidad TRK-102 excedió velocidad permitida: 94.8 km/h en Autopista MX-57D',
      timestamp: new Date(Date.now() - 3 * 60000).toISOString(),
      latitude: 20.5888,
      longitude: -100.3899,
      acknowledged: false
    },
    {
      id: 'ALT-902',
      vehicleId: 'TRK-104',
      severity: 'WARNING',
      ruleTriggered: 'LOW_FUEL_THRESHOLD (<15%)',
      message: 'Combustible crítico (12.4%) en unidad TRK-104. Autonomía restante: ~45 km',
      timestamp: new Date(Date.now() - 8 * 60000).toISOString(),
      latitude: 20.6736,
      longitude: -103.344,
      acknowledged: false
    },
    {
      id: 'ALT-903',
      vehicleId: 'TRK-101',
      severity: 'INFO',
      ruleTriggered: 'GEOFENCE_ENTRY_CONFIRMED',
      message: 'Unidad TRK-101 ingresó a perímetro seguro: Centro Logístico CDMX Norte',
      timestamp: new Date(Date.now() - 15 * 60000).toISOString(),
      latitude: 19.4326,
      longitude: -99.1332,
      acknowledged: true
    }
  ]);

  private alertSubject = new Subject<AlertNotification>();

  constructor() {
    this.initSseAlerts();
  }

  private initSseAlerts(): void {
    try {
      this.eventSource = new EventSource(`${this.baseUrl}/stream`);
      this.eventSource.onmessage = (event) => {
        const alert: AlertNotification = JSON.parse(event.data);
        this.activeAlerts.update(alerts => [alert, ...alerts]);
        this.alertSubject.next(alert);
        this.playAlertSound();
      };
    } catch (e) {
      console.log('SSE Alert stream local mode active');
    }
  }

  public acknowledgeAlert(id: string): void {
    this.activeAlerts.update(alerts => alerts.map(a => {
      if (a.id === id) {
        return { ...a, acknowledged: true };
      }
      return a;
    }));
  }

  public getAlertStream(): Observable<AlertNotification> {
    return this.alertSubject.asObservable();
  }

  private playAlertSound(): void {
    try {
      const audioCtx = new (window.AudioContext || (window as any).webkitAudioContext)();
      const osc = audioCtx.createOscillator();
      const gain = audioCtx.createGain();
      osc.type = 'sawtooth';
      osc.frequency.setValueAtTime(880, audioCtx.currentTime); // A5 note
      osc.frequency.exponentialRampToValueAtTime(440, audioCtx.currentTime + 0.15);
      gain.gain.setValueAtTime(0.2, audioCtx.currentTime);
      gain.gain.exponentialRampToValueAtTime(0.01, audioCtx.currentTime + 0.2);
      osc.connect(gain);
      gain.connect(audioCtx.destination);
      osc.start();
      osc.stop(audioCtx.currentTime + 0.2);
    } catch (e) {
      // Audio context might be restricted before user gesture
    }
  }
}
