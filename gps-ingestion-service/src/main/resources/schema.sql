CREATE TABLE IF NOT EXISTS telemetry_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    device_id VARCHAR(100) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    altitude DOUBLE PRECISION,
    speed_kmh DOUBLE PRECISION NOT NULL,
    heading DOUBLE PRECISION,
    battery_level DOUBLE PRECISION,
    fuel_level DOUBLE PRECISION,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_telemetry_vehicle_time ON telemetry_records (vehicle_id, timestamp);

-- Seed data for testing and immediate evaluation
INSERT INTO telemetry_records (vehicle_id, device_id, latitude, longitude, altitude, speed_kmh, heading, battery_level, fuel_level, timestamp)
SELECT 1, 'GPS-DEV-001', 19.432608, -99.133209, 2240.0, 65.5, 90.0, 95.0, 80.0, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM telemetry_records WHERE device_id = 'GPS-DEV-001');

INSERT INTO telemetry_records (vehicle_id, device_id, latitude, longitude, altitude, speed_kmh, heading, battery_level, fuel_level, timestamp)
SELECT 2, 'GPS-DEV-002', 20.659698, -103.349609, 1566.0, 82.0, 180.0, 88.0, 72.0, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM telemetry_records WHERE device_id = 'GPS-DEV-002');
