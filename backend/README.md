# NeuroFleetX Backend - Spring Boot REST API

A comprehensive AI-driven fleet management platform backend built with Spring Boot, MySQL, and JWT authentication.

## Project Structure

```
src/main/java/com/neurofleetx/
├── auth/                  # Authentication & User Management
├── fleet/                 # Vehicle Fleet Management
├── route/                 # Route Optimization Engine
├── dashboard/             # Dashboard Analytics
└── common/               # Shared utilities and DTOs
```

## Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Git

## Setup Instructions

### 1. Database Setup

```bash
# Create database
mysql -u root -p < src/main/resources/schema.sql

# Optionally load sample data
mysql -u root -p neurofleetx < src/main/resources/data.sql
```

### 2. Configure Database Connection

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/neurofleetx
    username: root
    password: your_password
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The API will be available at: `http://localhost:8080/api`

## API Endpoints

### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login user
- `GET /auth/me` - Get current user

### Fleet Management
- `GET /vehicles` - List all vehicles
- `POST /vehicles` - Create vehicle
- `GET /vehicles/{id}` - Get vehicle details
- `PUT /vehicles/{id}` - Update vehicle
- `DELETE /vehicles/{id}` - Delete vehicle

### Telemetry
- `GET /telemetry/vehicles/{vehicleId}/latest` - Latest telemetry
- `GET /telemetry/vehicles/{vehicleId}/history` - Telemetry history
- `POST /telemetry/vehicles/{vehicleId}/simulate` - Simulate telemetry

### Route Optimization
- `POST /routes/optimize` - Optimize route
- `GET /routes` - List routes
- `POST /routes` - Create route

### Dashboards
- `GET /dashboard/metrics/admin` - Admin metrics
- `GET /dashboard/metrics/manager` - Manager metrics
- `GET /dashboard/metrics/driver` - Driver metrics
- `GET /dashboard/metrics/customer` - Customer metrics

## Demo Users (in database)

```
Admin:
  Email: admin@neurofleetx.com
  Role: ADMIN
  Password: (hashed with bcrypt)

Fleet Manager:
  Email: manager@neurofleetx.com
  Role: FLEET_MANAGER

Driver:
  Email: driver@neurofleetx.com
  Role: DRIVER

Customer:
  Email: customer@neurofleetx.com
  Role: CUSTOMER
```

## Key Features

### Authentication & Authorization
- JWT-based stateless authentication
- Role-based access control (RBAC)
- Four user roles: Admin, Fleet Manager, Driver, Customer

### Fleet Management
- Complete CRUD operations for vehicles
- Real-time telemetry tracking
- Vehicle status management (Available, In Use, Maintenance)

### Route Optimization
- Haversine formula for distance calculation
- Three optimization strategies:
  1. Shortest distance route
  2. Fastest time route
  3. Most efficient (eco-friendly) route
- ETA estimation

### Dashboards
- Role-specific metrics and KPIs
- Real-time fleet status
- Historical data tracking

## Technology Stack

- Spring Boot 3.2.1
- Spring Security with JWT
- Spring Data JPA
- MySQL 8.0
- Lombok
- Maven

## CORS Configuration

The application is configured to accept requests from:
- `http://localhost:5173` (Vite dev server)
- `http://localhost:3000`
- `http://127.0.0.1:5173`

Modify `src/main/resources/application.yml` to add more origins if needed.

## Troubleshooting

### Database Connection Error
- Ensure MySQL is running
- Check database credentials in `application.yml`
- Verify database name is `neurofleetx`

### Port Already in Use
- Change port in `application.yml`:
```yaml
server:
  port: 8081
```

### JWT Token Issues
- Tokens expire after 24 hours (configurable in `application.yml`)
- Include `Authorization: Bearer {token}` in request headers

## Development

### Running Tests
```bash
mvn test
```

### Building Production JAR
```bash
mvn clean package
```

The JAR file will be in `target/neurofleetx-backend-1.0.0.jar`

## Deployment

### Docker (Optional)
Create a `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/neurofleetx-backend-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t neurofleetx-backend .
docker run -p 8080:8080 neurofleetx-backend
```

## API Response Format

All responses follow this format:
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { /* response data */ },
  "statusCode": 200
}
```

## Support & Documentation

For more information, see the main project README.md in the parent directory.

---
Built with ❤️ for NeuroFleetX
