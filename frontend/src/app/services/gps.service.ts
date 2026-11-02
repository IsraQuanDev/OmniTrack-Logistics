import { Injectable, signal } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { TelemetryPing } from '../models/omnitrack.models';

@Injectable({
  providedIn: 'root'
})
export class GpsService {
  private readonly baseUrl = 'http://localhost:8082/api/v1/telemetry';
  private eventSource: EventSource | null = null;
  
  public readonly latestPing = signal<TelemetryPing | null>(null);
  public readonly pingThroughput = signal<number>(14850); // pings/sec
  public readonly isStreamConnected = signal<boolean>(true);

  private telemetrySubject = new Subject<TelemetryPing>();

  constructor() {
    this.initStream();
  }

  public initStream(): void {
    try {
      this.eventSource = new EventSource(`${this.baseUrl}/stream`);
      
      this.eventSource.onopen = () => {
        this.isStreamConnected.set(true);
      };

      this.eventSource.onmessage = (event) => {
        const ping: TelemetryPing = JSON.parse(event.data);
        this.latestPing.set(ping);
        this.telemetrySubject.next(ping);
      };

      this.eventSource.onerror = () => {
        this.isStreamConnected.set(false);
        this.eventSource?.close();
      };
    } catch (e) {
      this.isStreamConnected.set(false);
    }
  }

  public getTelemetryStream(): Observable<TelemetryPing> {
    return this.telemetrySubject.asObservable();
  }
}
