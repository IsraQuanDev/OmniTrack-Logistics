import { Injectable, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { GeofenceZone, RouteCalculationRequest, RouteCalculationResponse } from '../models/omnitrack.models';

@Injectable({
  providedIn: 'root'
})
export class RouteService {
  private readonly baseUrl = 'http://localhost:8083/api/v1/routes';
  private readonly EARTH_RADIUS_KM = 6371.0;

  public readonly activeGeofences = signal<GeofenceZone[]>([
    {
      id: 'GF-01',
      name: 'Centro Logístico CDMX Norte (Depósito)',
      type: 'CIRCLE',
      coordinates: [[19.4326, -99.1332]],
      radiusKm: 15.0,
      severity: 'SAFE',
      status: 'ACTIVE'
    },
    {
      id: 'GF-02',
      name: 'Corredor Industrial México - Querétaro (MX-57D)',
      type: 'POLYGON',
      coordinates: [
        [19.55, -99.25],
        [20.10, -99.45],
        [20.65, -100.45],
        [20.55, -100.55],
        [19.45, -99.35]
      ],
      severity: 'WARNING',
      status: 'ACTIVE'
    },
    {
      id: 'GF-03',
      name: 'Zona Restringida de Alto Riesgo - Sector Bajío Sur',
      type: 'POLYGON',
      coordinates: [
        [20.45, -101.10],
        [20.80, -101.05],
        [20.75, -101.45],
        [20.40, -101.40]
      ],
      severity: 'CRITICAL',
      status: 'ACTIVE'
    }
  ]);

  constructor(private http: HttpClient) {}

  /**
   * Cálculo cliente-servidor de la fórmula de Haversine
   */
  public calculateHaversineDistance(lat1: number, lon1: number, lat2: number, lon2: number): number {
    const dLat = this.toRadians(lat2 - lat1);
    const dLon = this.toRadians(lon2 - lon1);
    const rLat1 = this.toRadians(lat1);
    const rLat2 = this.toRadians(lat2);

    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
              Math.cos(rLat1) * Math.cos(rLat2) *
              Math.sin(dLon / 2) * Math.sin(dLon / 2);

    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return parseFloat((this.EARTH_RADIUS_KM * c).toFixed(2));
  }

  /**
   * Algoritmo Ray-Casting para determinar si un punto está dentro de un polígono de geocerca
   */
  public isPointInPolygon(point: [number, number], polygon: [number, number][]): boolean {
    const [x, y] = point;
    let inside = false;
    for (let i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
      const xi = polygon[i][0], yi = polygon[i][1];
      const xj = polygon[j][0], yj = polygon[j][1];

      const intersect = ((yi > y) !== (yj > y)) &&
          (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
      if (intersect) inside = !inside;
    }
    return inside;
  }

  public calculateRoute(req: RouteCalculationRequest): Observable<RouteCalculationResponse> {
    const dist = this.calculateHaversineDistance(req.originLat, req.originLon, req.destLat, req.destLon);
    const avgSpeedKmH = 75.0; // Velocidad comercial promedio
    const travelTimeMinutes = Math.round((dist / avgSpeedKmH) * 60);

    const arrivalDate = new Date();
    arrivalDate.setMinutes(arrivalDate.getMinutes() + travelTimeMinutes);

    const response: RouteCalculationResponse = {
      distanceKm: dist,
      estimatedTravelTimeMinutes: travelTimeMinutes,
      estimatedArrivalIso: arrivalDate.toISOString(),
      waypoints: [
        [req.originLat, req.originLon],
        [(req.originLat + req.destLat) / 2 + 0.05, (req.originLon + req.destLon) / 2 - 0.02],
        [req.destLat, req.destLon]
      ],
      tollCostEstimatedMxn: Math.round(dist * 1.85),
      algorithmUsed: 'Haversine Orthodromic Spherical Trigonometry (R=6371km)'
    };

    return of(response);
  }

  private toRadians(degrees: number): number {
    return degrees * (Math.PI / 180);
  }
}
