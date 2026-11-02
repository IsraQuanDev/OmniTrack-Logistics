CREATE TABLE IF NOT EXISTS alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    alert_type VARCHAR(50) NOT NULL,
    severity VARCHAR(30) NOT NULL,
    message VARCHAR(255) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    current_value DOUBLE PRECISION,
    threshold_value DOUBLE PRECISION,
    resolved BOOLEAN NOT NULL DEFAULT FALSE,
    resolved_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS alert_rules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    rule_type VARCHAR(50) NOT NULL,
    threshold_value DOUBLE PRECISION NOT NULL,
    severity VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed rules for anomaly detection
INSERT INTO alert_rules (name, rule_type, threshold_value, severity, active)
SELECT 'Límite de Velocidad Máxima en Carretera', 'SPEED_LIMIT_EXCEEDED', 95.0, 'HIGH', TRUE
WHERE NOT EXISTS (SELECT 1 FROM alert_rules WHERE name = 'Límite de Velocidad Máxima en Carretera');

INSERT INTO alert_rules (name, rule_type, threshold_value, severity, active)
SELECT 'Batería de Rastreador Crítica', 'LOW_BATTERY', 15.0, 'MEDIUM', TRUE
WHERE NOT EXISTS (SELECT 1 FROM alert_rules WHERE name = 'Batería de Rastreador Crítica');

INSERT INTO alert_rules (name, rule_type, threshold_value, severity, active)
SELECT 'Nivel Crítico de Combustible', 'LOW_FUEL', 10.0, 'HIGH', TRUE
WHERE NOT EXISTS (SELECT 1 FROM alert_rules WHERE name = 'Nivel Crítico de Combustible');

-- Seed sample alerts
INSERT INTO alerts (vehicle_id, alert_type, severity, message, latitude, longitude, current_value, threshold_value, resolved)
SELECT 1, 'SPEED_LIMIT_EXCEEDED', 'HIGH', 'Vehículo excedió velocidad permitida: 105.2 km/h', 19.432608, -99.133209, 105.2, 95.0, FALSE
WHERE NOT EXISTS (SELECT 1 FROM alerts WHERE vehicle_id = 1 AND alert_type = 'SPEED_LIMIT_EXCEEDED');

INSERT INTO alerts (vehicle_id, alert_type, severity, message, latitude, longitude, current_value, threshold_value, resolved)
SELECT 2, 'LOW_BATTERY', 'MEDIUM', 'Batería baja en dispositivo GPS: 12.0%', 20.659698, -103.349609, 12.0, 15.0, TRUE
WHERE NOT EXISTS (SELECT 1 FROM alerts WHERE vehicle_id = 2 AND alert_type = 'LOW_BATTERY');
