CREATE TABLE IF NOT EXISTS app_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    full_name VARCHAR(150) NOT NULL,
    role VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS drivers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    license_number VARCHAR(50) NOT NULL UNIQUE,
    phone VARCHAR(30) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS vehicles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vin VARCHAR(50) NOT NULL UNIQUE,
    license_plate VARCHAR(20) NOT NULL UNIQUE,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    year_manufactured INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    capacity_kg DOUBLE PRECISION NOT NULL,
    assigned_driver_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS shipments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tracking_number VARCHAR(50) NOT NULL UNIQUE,
    vehicle_id BIGINT,
    origin_address VARCHAR(255) NOT NULL,
    origin_latitude DOUBLE PRECISION NOT NULL,
    origin_longitude DOUBLE PRECISION NOT NULL,
    dest_address VARCHAR(255) NOT NULL,
    dest_latitude DOUBLE PRECISION NOT NULL,
    dest_longitude DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) NOT NULL,
    estimated_delivery_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed data for testing and immediate evaluation (universal ANSI SQL)
INSERT INTO app_users (username, password, email, full_name, role, active)
SELECT 'admin', '$2a$10$wI5PjGgAonI7bVb7jN0tAOn5zBlnbE6z7m3gWvU2sL9oD5sX5I5Wy', 'admin@fleetplatform.com', 'Administrador del Sistema', 'ROLE_ADMIN', TRUE
WHERE NOT EXISTS (SELECT 1 FROM app_users WHERE username = 'admin');

INSERT INTO app_users (username, password, email, full_name, role, active)
SELECT 'operador', '$2a$10$wI5PjGgAonI7bVb7jN0tAOn5zBlnbE6z7m3gWvU2sL9oD5sX5I5Wy', 'operador@fleetplatform.com', 'Operador de Flota', 'ROLE_USER', TRUE
WHERE NOT EXISTS (SELECT 1 FROM app_users WHERE username = 'operador');

INSERT INTO drivers (full_name, license_number, phone, email, active)
SELECT 'Carlos Mendez', 'LIC-MX-98765', '+525512345678', 'carlos.mendez@fleetplatform.com', TRUE
WHERE NOT EXISTS (SELECT 1 FROM drivers WHERE license_number = 'LIC-MX-98765');

INSERT INTO drivers (full_name, license_number, phone, email, active)
SELECT 'Maria Rodriguez', 'LIC-MX-54321', '+525587654321', 'maria.rodriguez@fleetplatform.com', TRUE
WHERE NOT EXISTS (SELECT 1 FROM drivers WHERE license_number = 'LIC-MX-54321');

INSERT INTO vehicles (vin, license_plate, brand, model, year_manufactured, status, capacity_kg, assigned_driver_id)
SELECT '3VW12345678901234', 'ABC-1234', 'Volvo', 'FH16 750', 2023, 'ACTIVE', 25000.0, 1
WHERE NOT EXISTS (SELECT 1 FROM vehicles WHERE license_plate = 'ABC-1234');

INSERT INTO vehicles (vin, license_plate, brand, model, year_manufactured, status, capacity_kg, assigned_driver_id)
SELECT '1HD98765432109876', 'XYZ-5678', 'Freightliner', 'Cascadia', 2022, 'IN_TRANSIT', 28000.0, 2
WHERE NOT EXISTS (SELECT 1 FROM vehicles WHERE license_plate = 'XYZ-5678');

INSERT INTO shipments (tracking_number, vehicle_id, origin_address, origin_latitude, origin_longitude, dest_address, dest_latitude, dest_longitude, status)
SELECT 'TRK-2026-0001', 2, 'Centro de Distribución CDMX', 19.432608, -99.133209, 'Almacén Central Guadalajara', 20.659698, -103.349609, 'IN_TRANSIT'
WHERE NOT EXISTS (SELECT 1 FROM shipments WHERE tracking_number = 'TRK-2026-0001');
