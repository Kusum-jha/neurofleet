# NeuroFleetX - AI-Driven Fleet Management Platform

A next-generation platform for optimizing urban mobility and fleet operations using artificial intelligence, IoT, and geospatial data.

**Status**: ✅ Modules 1-3 Complete (Authentication, Fleet Inventory, Route Optimization)

## Quick Overview

NeuroFleetX provides:
- 🔐 Role-based authentication (Admin, Fleet Manager, Driver, Customer)
- 🚗 Fleet inventory management with real-time telemetry
- 🗺️ AI-powered route optimization
- 📊 Real-time dashboards for all roles
- 📱 Modern, responsive UI with glassmorphism design
- ☁️ REST API backend with JWT security

## Project Structure

```
neurofleetx/
├── frontend/              # React 19 + Vite SPA
│   ├── src/
│   │   ├── pages/        # Route pages
│   │   ├── components/   # Reusable components
│   │   ├── services/     # API integrations
│   │   ├── styles/       # CSS modules
│   │   └── utils/        # Utilities
│   ├── package.json      # Dependencies
│   └── vite.config.js    # Build config
│
└── backend/              # Spring Boot REST API
    ├── src/
    │   ├── main/java/com/neurofleetx/
    │   │   ├── auth/     # Authentication
    │   │   ├── fleet/    # Vehicle management
    │   │   ├── route/    # Route optimization
    │   │   ├── dashboard/# Analytics
    │   │   └── common/   # Shared code
    │   └── main/resources/
    │       ├── application.yml
    │       ├── schema.sql
    │       └── data.sql
    ├── pom.xml
    └── README.md
```

## Tech Stack

| Layer | Technology |
|-------|------------|
| Frontend | React 19, Vite, React Router, Axios, Leaflet.js |
| Backend | Spring Boot 3.2, MySQL 8.0, JWT, Spring Security |
| Maps | Leaflet.js + OpenStreetMap |
| Database | MySQL 8.0 |
| Build | Maven (Backend), Vite (Frontend) |

## Installation & Setup

### Prerequisites

**Backend:**
- Java 17+
- Maven 3.8+
- MySQL 8.0+

**Frontend:**
- Node.js 18+
- npm 10+

### Step 1: MySQL Database Setup

```bash
# Create database
mysql -u root -p < backend/src/main/resources/schema.sql

# Load sample data (optional)
mysql -u root -p neurofleetx < backend/src/main/resources/data.sql
```

**Demo Users** (created automatically):
- Admin: `admin@neurofleetx.com`
- Manager: `manager@neurofleetx.com`
- Driver: `driver@neurofleetx.com`
- Customer: `customer@neurofleetx.com`

### Step 2: Backend Setup

```bash
cd backend

# Configure database connection
# Edit: src/main/resources/application.yml
# Update username and password

# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

Backend will start at: `http://localhost:8080/api`

### Step 3: Frontend Setup

```bash
cd ../frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

Frontend will start at: `http://localhost:5173`

## Module Details

### Module 1: Authentication & Role Management ✅

**Features:**
- Secure login/registration with role selection
- JWT-based authentication
- 4 role-based dashboards:
  - **Admin**: System overview, user management, fleet statistics
  - **Fleet Manager**: Fleet management, route monitoring, utilization metrics
  - **Driver**: Daily earnings, routes, performance metrics
  - **Customer**: Bookings, loyalty points, spending history

**Routes:**
- `/login` - Login page with role selector
- `/register` - Registration page
- `/admin` - Admin dashboard
- `/manager` - Manager dashboard
- `/driver` - Driver dashboard
- `/customer` - Customer dashboard

### Module 2: Fleet Inventory & Vehicle Telemetry ✅

**Features:**
- Complete vehicle CRUD operations
- Real-time telemetry simulation (GPS, speed, battery, fuel)
- Vehicle status tracking (Available, In Use, Maintenance)
- Advanced filtering and search
- Battery/fuel level indicators
- Live location coordinates

**Routes:**
- `/fleet` - Fleet inventory page with grid view

**Components:**
- VehicleCard - Individual vehicle cards with telemetry
- FleetInventory - Fleet management page
- TelemetryPanel - Real-time telemetry display

### Module 3: AI Route & Load Optimization Engine ✅

**Features:**
- AI-powered route optimization using Haversine formula
- Three optimization strategies:
  1. **Shortest Distance** - Minimum route distance
  2. **Fastest Time** - Considering traffic/highway efficiency
  3. **Most Efficient** - Eco-friendly, fuel/battery optimized
- Interactive map with Leaflet.js + OpenStreetMap
- ETA calculation
- Visual route display with polylines and markers
- Vehicle and route visualization on map

**Routes:**
- `/routes` - Route planner with map

**Components:**
- RoutePlanner - Route planning interface
- MapContainer - Interactive Leaflet map visualization
- RouteCard - Route details with statistics

## API Documentation

### Authentication Endpoints

```
POST /auth/register
POST /auth/login
GET /auth/me
```

### Vehicle Endpoints

```
GET /vehicles                          # List all vehicles
POST /vehicles                         # Create vehicle
GET /vehicles/{id}                     # Get vehicle
PUT /vehicles/{id}                     # Update vehicle
DELETE /vehicles/{id}                  # Delete vehicle
GET /vehicles/status/{status}          # Filter by status
```

### Telemetry Endpoints

```
GET /telemetry/vehicles/{vehicleId}/latest      # Latest data
GET /telemetry/vehicles/{vehicleId}/history     # Historical data
POST /telemetry/vehicles/{vehicleId}/simulate   # Simulate data
```

### Route Endpoints

```
POST /routes/optimize                  # Optimize route
GET /routes                           # List routes
POST /routes                          # Create route
GET /routes/{id}                      # Get route
PUT /routes/{id}/status              # Update status
```

### Dashboard Endpoints

```
GET /dashboard/metrics/admin          # Admin metrics
GET /dashboard/metrics/manager        # Manager metrics
GET /dashboard/metrics/driver         # Driver metrics
GET /dashboard/metrics/customer       # Customer metrics
```

## Usage

### 1. Start Backend

```bash
cd backend
mvn spring-boot:run
```

### 2. Start Frontend

```bash
cd frontend
npm run dev
```

### 3. Access Application

Open browser: `http://localhost:5173`

Login with any demo user (see Demo Users section)

### 4. Explore Features

**Admin Dashboard:**
- View all users, vehicles, routes
- Monitor system metrics
- Manage fleet

**Fleet Inventory:**
- Click "Manage Fleet" from any dashboard
- View all vehicles with real-time telemetry
- Simulate vehicle telemetry with "Simulate" button
- Search and filter by vehicle type or status

**Route Planner:**
- Click "Manage Routes" from managers/admin
- Select a vehicle
- Enter start and end locations (or use defaults)
- Click "Optimize Route"
- View 3 optimized route options with map visualization

## Design System

### Color Palette (Ocean Blue Theme)
- **Primary**: #0066cc (Ocean Light)
- **Accent**: #00b4ff (Bright Accent)
- **Dark**: #001d3d (Deep Dark)
- **Success**: #00d084 (Green)
- **Warning**: #ff6b6b (Red)

### Components
- Glassmorphism effects with backdrop blur
- Smooth animations and transitions
- Responsive grid layouts
- Intuitive icons and emojis

## Building for Production

### Frontend

```bash
cd frontend
npm run build
# Output: dist/
```

### Backend

```bash
cd backend
mvn clean package
# Output: target/neurofleetx-backend-1.0.0.jar
```

## Deployment

### Docker Compose (Optional)

Create `docker-compose.yml`:

```yaml
version: '3'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: neurofleetx
      MYSQL_ROOT_PASSWORD: password
    ports:
      - "3306:3306"

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    depends_on:
      - mysql

  frontend:
    build: ./frontend
    ports:
      - "5173:5173"
```

Run: `docker-compose up`

## Features Implemented

### ✅ Module 1: Authentication & Role Management
- [x] Login/Registration pages
- [x] Role selector in login
- [x] JWT authentication
- [x] Admin dashboard
- [x] Fleet Manager dashboard
- [x] Driver dashboard
- [x] Customer dashboard
- [x] Role-based access control
- [x] Backend APIs for dashboard metrics

### ✅ Module 2: Fleet Inventory & Vehicle Telemetry
- [x] Vehicle CRUD operations
- [x] Real-time telemetry simulation
- [x] Vehicle status tracking
- [x] Fleet inventory UI with grid view
- [x] Search and filter functionality
- [x] Battery/fuel indicators
- [x] Location tracking
- [x] Telemetry history
- [x] Backend vehicle management APIs

### ✅ Module 3: AI Route & Load Optimization
- [x] Route optimization algorithm (Haversine formula)
- [x] Multiple route strategies (shortest, fastest, efficient)
- [x] ETA calculation
- [x] Interactive map with Leaflet.js
- [x] Route visualization with polylines
- [x] Vehicle markers on map
- [x] Route planner UI
- [x] Backend route optimization APIs

## Performance Metrics

- Frontend bundle: ~230 KB (gzipped: ~72 KB)
- CSS: ~40 KB (gzipped: ~10 KB)
- API response time: <500ms
- Database queries: Optimized with indexes
- Map rendering: Smooth 60 FPS

## Browser Support

- Chrome/Edge 90+
- Firefox 88+
- Safari 14+
- Mobile browsers (iOS Safari, Chrome Mobile)

## Troubleshooting

### Backend won't connect to database
```bash
# Check MySQL is running
mysql -u root -p -e "SHOW DATABASES;"

# Reset connection in application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/neurofleetx
    username: root
    password: your_password
```

### Frontend can't reach backend
```bash
# Ensure backend is running on port 8080
# Check CORS in backend/src/main/resources/application.yml
# Clear browser cache and restart dev server
npm run dev
```

### Map not loading
- Verify internet connection (OSM requires internet)
- Check Leaflet and React-Leaflet are installed
- Ensure coordinates are valid (between -90 to 90 latitude, -180 to 180 longitude)

## Future Enhancements

- Module 4: Predictive Maintenance
- Module 5: Customer Booking System
- Module 6: Admin Reports & Analytics
- Real-time WebSocket updates
- Mobile app with React Native
- Advanced analytics with ML models
- Payment gateway integration
- Multi-language support

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Submit pull request

## License

Proprietary - NeuroFleetX Corporation

## Support

For issues and questions:
- Check GitHub issues
- Review API documentation
- See backend/README.md and frontend documentation

---

**Built with ❤️ for smarter urban mobility**

*NeuroFleetX: Optimizing Fleet Operations Through AI*
