-- Sample Users (passwords are plain text for demo, use bcrypt in production)
INSERT IGNORE INTO users (username, email, password, role_id, first_name, last_name, phone, location, bio) VALUES
('admin_user', 'admin@neurofleetx.com', '$2a$10$R9h7cIPz0gi.URNNX3kh2OPST9/PgBkpHF7lrVEOJJG3G4ycNiM1C', 1, 'Admin', 'User', '9000000000', 'Fleet Headquarters', 'System Administrator'),
('manager_user', 'manager@neurofleetx.com', '$2a$10$R9h7cIPz0gi.URNNX3kh2OPST9/PgBkpHF7lrVEOJJG3G4ycNiM1C', 2, 'Fleet', 'Manager', '9100000000', 'Distribution Center', 'Fleet Operations Manager'),
('driver_user', 'driver@neurofleetx.com', '$2a$10$R9h7cIPz0gi.URNNX3kh2OPST9/PgBkpHF7lrVEOJJG3G4ycNiM1C', 3, 'John', 'Driver', '9200000000', 'City Center', 'Professional Driver'),
('customer_user', 'customer@neurofleetx.com', '$2a$10$R9h7cIPz0gi.URNNX3kh2OPST9/PgBkpHF7lrVEOJJG3G4ycNiM1C', 4, 'Jane', 'Customer', '9300000000', 'Downtown', 'Customer User');

-- Sample Vehicles
INSERT IGNORE INTO vehicles (vehicle_number, status, vehicle_type, capacity, fuel_type, battery_percentage, fuel_percentage, current_latitude, current_longitude, speed_kmh, mileage_km, last_maintenance) VALUES
('NF-EV-001', 'AVAILABLE', 'Electric', 5, 'Electric', 85, NULL, 28.6139, 77.2090, 0, 15000, '2024-01-15'),
('NF-EV-002', 'IN_USE', 'Electric', 5, 'Electric', 60, NULL, 28.5355, 77.3910, 45, 18000, '2023-12-20'),
('NF-GAS-001', 'AVAILABLE', 'Sedan', 5, 'Petrol', NULL, 90, 28.5921, 77.0460, 0, 25000, '2024-02-01'),
('NF-GAS-002', 'MAINTENANCE', 'SUV', 7, 'Petrol', NULL, 45, 28.7041, 77.1025, 0, 32000, '2024-01-10'),
('NF-TRUCK-001', 'IN_USE', 'Truck', 15, 'Diesel', NULL, 75, 28.4595, 77.0266, 55, 45000, '2024-02-05'),
('NF-AUTO-001', 'AVAILABLE', 'Auto Rickshaw', 3, 'CNG', NULL, 80, 28.6162, 77.2310, 0, 12000, '2024-01-25');

-- Sample Routes
INSERT IGNORE INTO routes (vehicle_id, start_latitude, start_longitude, end_latitude, end_longitude, distance_km, estimated_duration_minutes, status, created_at) VALUES
(1, 28.6139, 77.2090, 28.5355, 77.3910, 22.5, 45, 'COMPLETED', NOW() - INTERVAL 2 DAY),
(2, 28.5355, 77.3910, 28.4595, 77.0266, 35.8, 65, 'IN_PROGRESS', NOW() - INTERVAL 1 HOUR),
(3, 28.5921, 77.0460, 28.7041, 77.1025, 18.3, 35, 'PLANNED', NOW()),
(5, 28.4595, 77.0266, 28.6139, 77.2090, 42.1, 75, 'IN_PROGRESS', NOW() - INTERVAL 3 HOUR);

-- Sample Route Stops
INSERT IGNORE INTO route_stops (route_id, stop_order, latitude, longitude, description, eta) VALUES
(2, 1, 28.5355, 77.3910, 'pickup_location', NOW() + INTERVAL 30 MINUTE),
(2, 2, 28.5500, 77.3500, 'stop_1', NOW() + INTERVAL 50 MINUTE),
(2, 3, 28.4595, 77.0266, 'delivery_location', NOW() + INTERVAL 65 MINUTE),
(4, 1, 28.4595, 77.0266, 'pickup_location', NOW() + INTERVAL 20 MINUTE),
(4, 2, 28.5500, 77.1500, 'stop_1', NOW() + INTERVAL 45 MINUTE),
(4, 3, 28.6139, 77.2090, 'delivery_location', NOW() + INTERVAL 75 MINUTE);

-- Sample Telemetry Data
INSERT IGNORE INTO telemetry (vehicle_id, latitude, longitude, speed_kmh, battery_percentage, fuel_percentage, temperature_celsius, recorded_at) VALUES
(1, 28.6139, 77.2090, 0, 85, NULL, 28, NOW() - INTERVAL 5 MINUTE),
(2, 28.5380, 77.3900, 45, 60, NULL, 32, NOW() - INTERVAL 2 MINUTE),
(3, 28.5921, 77.0460, 0, NULL, 90, 27, NOW() - INTERVAL 10 MINUTE),
(4, 28.7041, 77.1025, 0, NULL, 45, 25, NOW() - INTERVAL 1 MINUTE),
(5, 28.5000, 77.1500, 55, NULL, 75, 35, NOW() - INTERVAL 3 MINUTE),
(6, 28.6162, 77.2310, 0, NULL, 80, 26, NOW() - INTERVAL 7 MINUTE);
