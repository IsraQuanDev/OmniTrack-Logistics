import { Injectable, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { EndpointPreset } from '../models/omnitrack.models';

export interface ApiExecutionResult {
  status: number;
  statusText: string;
  durationMs: number;
  timestamp: string;
  data: any;
  headers: Record<string, string>;
  isError: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ApiRunnerService {
  public readonly presets: EndpointPreset[] = [
    {
      serviceName: 'Fleet Management Service',
      port: 8081,
      method: 'GET',
      endpoint: '/api/v1/fleet/vehicles',
      description: 'Obtiene el inventario completo de camiones y estado de disponibilidad',
      requiresAuth: true
    },
    {
      serviceName: 'Fleet Management Service',
      port: 8081,
      method: 'POST',
      endpoint: '/api/v1/fleet/vehicles',
      description: 'Registra un nuevo vehículo en la flota reactiva',
      samplePayload: JSON.stringify({
        plate: 'CDMX-998-TK',
        model: 'Kenworth T880 2025',
        driverName: 'Alejandro Domínguez',
        status: 'ACTIVE'
      }, null, 2),
      requiresAuth: true
    },
    {
      serviceName: 'GPS Ingestion Service',
      port: 8082,
      method: 'POST',
      endpoint: '/api/v1/telemetry/ingest',
      description: 'Ingesta un ping de telemetría de alta frecuencia (WebFlux Flux)',
      samplePayload: JSON.stringify({
        vehicleId: 'TRK-101',
        latitude: 19.432608,
        longitude: -99.133209,
        speed: 88.5,
        fuelLevel: 75.0,
        engineTemperature: 92.4,
        timestamp: new Date().toISOString()
      }, null, 2),
      requiresAuth: true
    },
    {
      serviceName: 'GPS Ingestion Service',
      port: 8082,
      method: 'GET',
      endpoint: '/api/v1/telemetry/latest/TRK-101',
      description: 'Recupera el último registro telemétrico en memoria de un vehículo',
      requiresAuth: true
    },
    {
      serviceName: 'Route Calculation Service',
      port: 8083,
      method: 'POST',
      endpoint: '/api/v1/routes/calculate',
      description: 'Calcula distancia geodésica Haversine y ETA entre dos coordenadas',
      samplePayload: JSON.stringify({
        originLat: 19.4326,
        originLon: -99.1332,
        destLat: 20.5888,
        destLon: -100.3899,
        vehicleId: 'TRK-101'
      }, null, 2),
      requiresAuth: true
    },
    {
      serviceName: 'Alert Service',
      port: 8084,
      method: 'GET',
      endpoint: '/api/v1/alerts/active',
      description: 'Consulta las alertas de telemetría pendientes y reglas infringidas',
      requiresAuth: true
    }
  ];

  public readonly lastExecution = signal<ApiExecutionResult | null>(null);

  constructor(private http: HttpClient) {}

  public executeRequest(
    method: 'GET' | 'POST' | 'PUT' | 'DELETE',
    url: string,
    payload?: string,
    username = 'admin',
    password = 'admin123'
  ): Observable<ApiExecutionResult> {
    const startTime = performance.now();
    const headers = new HttpHeaders({
      'Authorization': 'Basic ' + btoa(`${username}:${password}`),
      'Content-Type': 'application/json'
    });

    let req$: Observable<any>;
    const body = payload ? JSON.parse(payload) : null;

    switch (method) {
      case 'GET':
        req$ = this.http.get(url, { headers, observe: 'response' });
        break;
      case 'POST':
        req$ = this.http.post(url, body, { headers, observe: 'response' });
        break;
      case 'PUT':
        req$ = this.http.put(url, body, { headers, observe: 'response' });
        break;
      case 'DELETE':
        req$ = this.http.delete(url, { headers, observe: 'response' });
        break;
    }

    return new Observable(observer => {
      req$.pipe(
        catchError(err => of(err))
      ).subscribe(res => {
        const endTime = performance.now();
        const durationMs = Math.round(endTime - startTime);
        const isError = res.status >= 400 || !res.status;

        const result: ApiExecutionResult = {
          status: res.status || (isError ? 503 : 200),
          statusText: res.statusText || (isError ? 'Service Error' : 'OK'),
          durationMs,
          timestamp: new Date().toISOString(),
          data: res.body || res.error || { message: 'Operación ejecutada con éxito (Modo Reactivo)' },
          headers: {
            'content-type': 'application/json;charset=UTF-8',
            'x-reactive-engine': 'Netty-WebFlux'
          },
          isError
        };

        this.lastExecution.set(result);
        observer.next(result);
        observer.complete();
      });
    });
  }
}
