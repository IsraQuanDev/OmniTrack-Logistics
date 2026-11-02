// ============================================================================
// OMNITRACK LOGISTICS - TypeScript Domain Models
// ============================================================================

export interface Vehicle {
  id: string;
  plate: string;
  model: string;
  driverName: string;
  status: 'ACTIVE' | 'MAINTENANCE' | 'INACTIVE';
  latitude: number;
  longitude: number;
  speed: number;
  fuelLevel: number;
  engineTemperature: number;
  lastPingTime: string;
  currentRoute?: string;
}

export interface TelemetryPing {
  vehicleId: string;
  timestamp: string;
  latitude: number;
  longitude: number;
  speed: number;
  fuelLevel: number;
  engineTemperature: number;
  heading: number;
  altitude: number;
}

export interface RouteCalculationRequest {
  originLat: number;
  originLon: number;
  destLat: number;
  destLon: number;
  vehicleId?: string;
}

export interface RouteCalculationResponse {
  distanceKm: number;
  estimatedTravelTimeMinutes: number;
  estimatedArrivalIso: string;
  waypoints: [number, number][];
  tollCostEstimatedMxn: number;
  algorithmUsed: string;
}

export interface GeofenceZone {
  id: string;
  name: string;
  type: 'CIRCLE' | 'POLYGON';
  coordinates: [number, number][];
  radiusKm?: number;
  severity: 'SAFE' | 'WARNING' | 'CRITICAL';
  status: 'ACTIVE' | 'DISABLED';
}

export interface AlertNotification {
  id: string;
  vehicleId: string;
  severity: 'CRITICAL' | 'WARNING' | 'INFO';
  ruleTriggered: string;
  message: string;
  timestamp: string;
  latitude: number;
  longitude: number;
  acknowledged: boolean;
}

export interface EndpointPreset {
  serviceName: string;
  port: number;
  method: 'GET' | 'POST' | 'PUT' | 'DELETE';
  endpoint: string;
  description: string;
  samplePayload?: string;
  requiresAuth: boolean;
}
