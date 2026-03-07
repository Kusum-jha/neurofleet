-- Create database
CREATE DATABASE IF NOT EXISTS neurofleetx;
USE neurofleetx;

-- Roles Table
CREATE TABLE IF NOT EXISTS roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) UNIQUE NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Users Table
CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role_id BIGINT NOT NULL,
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  phone VARCHAR(20),
  location VARCHAR(255),
  bio TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (role_id) REFERENCES roles(id),
  INDEX idx_email (email),
  INDEX idx_role (role_id)
);

-- Vehicles Table
CREATE TABLE IF NOT EXISTS vehicles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  vehicle_number VARCHAR(50) UNIQUE NOT NULL,
  status VARCHAR(20) DEFAULT 'AVAILABLE',
  vehicle_type VARCHAR(50),
  capacity INT,
  fuel_type VARCHAR(50),
  battery_percentage INT DEFAULT 100,
  fuel_percentage INT DEFAULT 100,
  current_latitude DECIMAL(10, 8),
  current_longitude DECIMAL(11, 8),
  speed_kmh INT DEFAULT 0,
  mileage_km INT DEFAULT 0,
  last_maintenance DATE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_status (status),
  INDEX idx_vehicle_type (vehicle_type),
  INDEX idx_location (current_latitude, current_longitude)
);

-- Telemetry Table
CREATE TABLE IF NOT EXISTS telemetry (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  vehicle_id BIGINT NOT NULL,
  latitude DECIMAL(10, 8),
  longitude DECIMAL(11, 8),
  speed_kmh INT DEFAULT 0,
  battery_percentage INT,
  fuel_percentage INT,
  temperature_celsius INT,
  recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
  INDEX idx_vehicle_time (vehicle_id, recorded_at)
);

-- Routes Table
CREATE TABLE IF NOT EXISTS routes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  vehicle_id BIGINT NOT NULL,
  start_latitude DECIMAL(10, 8),
  start_longitude DECIMAL(11, 8),
  end_latitude DECIMAL(10, 8),
  end_longitude DECIMAL(11, 8),
  distance_km DECIMAL(10, 2),
  estimated_duration_minutes INT,
  actual_duration_minutes INT,
  status VARCHAR(20) DEFAULT 'PLANNED',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
  INDEX idx_status (status),
  INDEX idx_vehicle (vehicle_id),
  INDEX idx_created (created_at)
);

-- Route Stops Table
CREATE TABLE IF NOT EXISTS route_stops (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  route_id BIGINT NOT NULL,
  stop_order INT,
  latitude DECIMAL(10, 8),
  longitude DECIMAL(11, 8),
  description VARCHAR(255),
  eta TIMESTAMP,
  arrived_at TIMESTAMP,
  FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE,
  INDEX idx_route (route_id),
  INDEX idx_order (stop_order)
);

-- Module 4: Maintenance Table
CREATE TABLE IF NOT EXISTS maintenance_records (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  vehicle_id BIGINT NOT NULL,
  maintenance_type VARCHAR(50) NOT NULL,
  description TEXT,
  status VARCHAR(20) DEFAULT 'SCHEDULED',
  scheduled_date DATE NOT NULL,
  completed_date DATE,
  cost DECIMAL(10, 2),
  notes TEXT,
  created_by BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
  FOREIGN KEY (created_by) REFERENCES users(id),
  INDEX idx_vehicle_status (vehicle_id, status),
  INDEX idx_scheduled_date (scheduled_date)
);

-- Maintenance Alerts Table
CREATE TABLE IF NOT EXISTS maintenance_alerts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  vehicle_id BIGINT NOT NULL,
  alert_type VARCHAR(50) NOT NULL,
  severity VARCHAR(20) DEFAULT 'MEDIUM',
  message TEXT NOT NULL,
  resolved BOOLEAN DEFAULT FALSE,
  resolved_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
  INDEX idx_vehicle_resolved (vehicle_id, resolved),
  INDEX idx_severity (severity)
);

-- Module 5: Bookings Table
CREATE TABLE IF NOT EXISTS bookings (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  booking_number VARCHAR(50) UNIQUE NOT NULL,
  vehicle_id BIGINT NOT NULL,
  customer_id BIGINT NOT NULL,
  pickup_latitude DECIMAL(10, 8),
  pickup_longitude DECIMAL(11, 8),
  dropoff_latitude DECIMAL(10, 8),
  dropoff_longitude DECIMAL(11, 8),
  pickup_time TIMESTAMP NOT NULL,
  dropoff_time TIMESTAMP,
  estimated_fare DECIMAL(10, 2),
  actual_fare DECIMAL(10, 2),
  status VARCHAR(20) DEFAULT 'PENDING',
  payment_status VARCHAR(20) DEFAULT 'PENDING',
  distance_km DECIMAL(10, 2),
  duration_minutes INT,
  notes TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
  FOREIGN KEY (customer_id) REFERENCES users(id),
  INDEX idx_customer_status (customer_id, status),
  INDEX idx_vehicle_status (vehicle_id, status),
  INDEX idx_pickup_time (pickup_time),
  INDEX idx_status (status)
);

-- Loyalty Points Table
CREATE TABLE IF NOT EXISTS loyalty_points (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  customer_id BIGINT NOT NULL,
  points_balance INT DEFAULT 0,
  total_points_earned INT DEFAULT 0,
  total_points_redeemed INT DEFAULT 0,
  last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE,
  UNIQUE KEY (customer_id)
);

-- Module 6: Analytics Tables (Reports and Statistics)
CREATE TABLE IF NOT EXISTS daily_analytics (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  analytics_date DATE NOT NULL,
  total_bookings INT DEFAULT 0,
  completed_bookings INT DEFAULT 0,
  total_distance_km DECIMAL(12, 2) DEFAULT 0,
  total_revenue DECIMAL(12, 2) DEFAULT 0,
  active_vehicles INT DEFAULT 0,
  available_vehicles INT DEFAULT 0,
  maintenance_vehicles INT DEFAULT 0,
  avg_vehicle_utilization DECIMAL(5, 2) DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY (analytics_date)
);

-- Query Logs Table (for analytics)
CREATE TABLE IF NOT EXISTS query_logs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  query_type VARCHAR(50),
  vehicle_id BIGINT,
  customer_id BIGINT,
  status VARCHAR(20),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_date (created_at),
  INDEX idx_query_type (query_type)
);

-- Insert default roles
INSERT IGNORE INTO roles (name) VALUES ('ADMIN');
INSERT IGNORE INTO roles (name) VALUES ('FLEET_MANAGER');
INSERT IGNORE INTO roles (name) VALUES ('DRIVER');
INSERT IGNORE INTO roles (name) VALUES ('CUSTOMER');
