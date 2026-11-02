CREATE TABLE IF NOT EXISTS geofences (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(255),
    center_latitude DOUBLE PRECISION NOT NULL,
    center_longitude DOUBLE PRECISION NOT NULL,
    radius_meters DOUBLE PRECISION NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    vehicle_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS route_calculations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    origin_latitude DOUBLE PRECISION,
    origin_longitude DOUBLE PRECISION,
    dest_latitude DOUBLE PRECISION NOT NULL,
    dest_longitude DOUBLE PRECISION NOT NULL,
    current_latitude DOUBLE PRECISION NOT NULL,
    current_longitude DOUBLE PRECISION NOT NULL,
    current_speed_kmh DOUBLE PRECISION NOT NULL,
    distance_remaining_km DOUBLE PRECISION NOT NULL,
    eta_minutes DOUBLE PRECISION NOT NULL,
    estimated_arrival_timestamp TIMESTAMP NOT NULL,
    calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed geofence data
INSERT INTO geofences (name, description, center_latitude, center_longitude, radius_meters, active, vehicle_id)
SELECT 'Zona Metropolitana CDMX', 'Perímetro logístico de la capital', 19.432608, -99.133209, 15000.0, TRUE, 1
WHERE NOT EXISTS (SELECT 1 FROM geofences WHERE name = 'Zona Metropolitana CDMX');

INSERT INTO geofences (name, description, center_latitude, center_longitude, radius_meters, active, vehicle_id)
SELECT 'Corredor Industrial Guadalajara', 'Zona de descarga en Jalisco', 20.659698, -103.349609, 8000.0, TRUE, 2
WHERE NOT EXISTS (SELECT 1 FROM geofences WHERE name = 'Corredor Industrial Guadalajara');
