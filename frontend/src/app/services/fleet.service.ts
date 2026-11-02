import { Injectable, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Vehicle } from '../models/omnitrack.models';

@Injectable({
  providedIn: 'root'
})
export class FleetService {
  private readonly baseUrl = 'http://localhost:8081/api/v1/fleet';

  // Reactive state using Angular Signals
  public readonly vehicles = signal<Vehicle[]>([
    {
      id: 'TRK-101',
      plate: 'MX-442-AA',
      model: 'Kenworth T680 2024',
      driverName: 'Carlos Mendoza',
      status: 'ACTIVE',
      latitude: 19.4326,
      longitude: -99.1332,
      speed: 86.4,
      fuelLevel: 78.2,
      engineTemperature: 91.5,
      lastPingTime: new Date().toISOString(),
      currentRoute: 'CDMX Centro -> Hub Norte Querétaro'
    },
    {
      id: 'TRK-102',
      plate: 'NL-881-BZ',
      model: 'Freightliner Cascadia',
      driverName: 'Juan Carlos Pérez',
      status: 'ACTIVE',
      latitude: 20.5888,
      longitude: -100.3899,
      speed: 94.8, // Speeding
      fuelLevel: 42.5,
      engineTemperature: 94.2,
      lastPingTime: new Date().toISOString(),
      currentRoute: 'Querétaro -> San Luis Potosí'
    },
    {
      id: 'TRK-103',
      plate: 'JAL-553-CX',
      model: 'Volvo VNL 860',
      driverName: 'Roberto Gómez',
      status: 'ACTIVE',
      latitude: 25.6866,
      longitude: -100.3161,
      speed: 72.1,
      fuelLevel: 89.0,
      engineTemperature: 88.0,
      lastPingTime: new Date().toISOString(),
      currentRoute: 'Monterrey Hub -> Laredo Border'
    },
    {
      id: 'TRK-104',
      plate: 'QRO-902-DX',
      model: 'International LT625',
      driverName: 'Esteban Morales',
      status: 'ACTIVE',
      latitude: 20.6736,
      longitude: -103.344,
      speed: 65.0,
      fuelLevel: 12.4, // Low Fuel
      engineTemperature: 96.1,
      lastPingTime: new Date().toISOString(),
      currentRoute: 'Guadalajara -> Manzanillo Puerto'
    }
  ]);

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': 'Basic ' + btoa('admin:admin123'),
      'Content-Type': 'application/json'
    });
  }

  getVehicles(): Observable<Vehicle[]> {
    // If backend is live, fetch from microservice; otherwise fallback to local signal state
    return this.http.get<Vehicle[]>(`${this.baseUrl}/vehicles`, { headers: this.getAuthHeaders() });
  }

  updateVehicleLocation(vehicleId: string, lat: number, lon: number, speed: number, fuel: number): void {
    this.vehicles.update(list => list.map(v => {
      if (v.id === vehicleId) {
        return {
          ...v,
          latitude: lat,
          longitude: lon,
          speed: speed,
          fuelLevel: fuel,
          lastPingTime: new Date().toISOString()
        };
      }
      return v;
    }));
  }
}
