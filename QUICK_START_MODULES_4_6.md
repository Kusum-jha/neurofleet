# Quick Start Guide - Modules 4, 5, 6

## 🚀 Getting Started with Modules 4-6

### Prerequisites
Ensure Modules 1-3 are working before starting Modules 4-6:
- Backend running on `http://localhost:8080/api`
- Frontend running on `http://localhost:5173`
- MySQL database with schema loaded

### Setup Instructions

#### Step 1: Load Database Schema
The new tables for modules 4-6 are already in `schema.sql`. If using existing database:

```bash
# Load the complete schema (includes all modules)
mysql -u root -p < backend/src/main/resources/schema.sql
```

#### Step 2: Build Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend will automatically create all new tables from the schema.

#### Step 3: Install Frontend Dependencies
```bash
cd neurofleetx
npm install
npm run dev
```

---

## 📱 Module 4: Predictive Maintenance

### How to Access
1. Login as **Admin** (admin@neurofleetx.com / Admin@123)
2. Navigate to **Maintenance** link in navbar (or `/maintenance`)

### Key Features

#### View Maintenance Records
- Tab: "📋 Maintenance Records"
- Shows all scheduled, in-progress, and completed maintenance
- Status indicators with color coding
- Complete maintenance records button

**Status Colors**:
- 🔵 SCHEDULED (Blue #4da6ff)
- 🟠 IN_PROGRESS (Orange #ff9500)
- 🟢 COMPLETED (Green #00d084)
- 🔴 CANCELLED (Red #ff6b6b)

#### Manage Alerts
- Tab: "⚠️ Alerts"
- Automatic alerts for:
  - **LOW_BATTERY** (< 20%)
  - **LOW_FUEL** (< 20%)
  - **HIGH_MILEAGE** (> 50,000 km)
  - **OVERDUE_MAINTENANCE** (> 6 months)

**Alert Severity**:
- 🟢 LOW - Green
- 🟠 MEDIUM - Orange
- 🔴 HIGH - Red
- ⚫ CRITICAL - Dark Red

#### Create New Record
- Tab: "➕ Create Record"
- Enter:
  - Vehicle ID (e.g., 1, 2, 3)
  - Maintenance Type (Oil Change, Tire Rotation, etc.)
  - Description (optional)
  - Scheduled Date
  - Estimated Cost (optional)

#### Statistics Dashboard
Shows real-time metrics:
- Total Scheduled Maintenance
- Maintenance In Progress
- Completed Count
- Unresolved Alerts
- Total Maintenance Cost

### API Endpoints

```bash
# Get all records
curl http://localhost:8080/api/maintenance/records

# Create record
curl -X POST http://localhost:8080/api/maintenance/records \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleId": 1,
    "maintenanceType": "Oil Change",
    "description": "Regular oil change service",
    "scheduledDate": "2024-03-15",
    "cost": 2500
  }'

# Complete a record
curl -X PUT http://localhost:8080/api/maintenance/records/1/complete

# Generate alerts
curl -X POST http://localhost:8080/api/maintenance/alerts/generate

# Get vehicle predictions
curl http://localhost:8080/api/maintenance/predict/1
```

---

## 🚗 Module 5: Customer Booking System

### How to Access
1. Login as any user
2. Navigate to **Bookings** link in navbar (or `/bookings`)

### Key Features

#### View Your Bookings
- Shows all bookings created by the logged-in user
- Displays:
  - Booking Number (auto-generated)
  - Vehicle Number
  - Status (PENDING, IN_PROGRESS, COMPLETED, CANCELLED)
  - Pickup/Dropoff Times
  - Estimated & Actual Fare

**Status Flow**:
```
PENDING → (Cancel or confirm) → IN_PROGRESS → (Complete) → COMPLETED
```

#### Create a New Booking
- Tab: "➕ New Booking"
- Required fields:
  - **Vehicle ID**: Enter 1-5 (from fleet inventory)
  - **Pickup Location**: Lat/Long (e.g., 28.7041, 77.1025 for Delhi)
  - **Dropoff Location**: Lat/Long (e.g., 28.6139, 77.2090)
  - **Pickup Time**: Date and time in ISO format
  - **Distance**: km (optional)

**Example Coordinates** (Delhi, India):
- Center: 28.7041, 77.1025
- Nearby: 28.6139, 77.2090 (South Delhi)
- Northern: 28.7500, 77.1200 (North Delhi)

#### Loyalty Points System
- **Earn**: 1 point per rupee spent
- **View**: Current balance, total earned, total redeemed
- **Redeem**: 100 or 500 points for discounts
- Shows in top card with "⭐ Your Loyalty Points"

**Fare Calculation**:
```
Distance (km) × ₹15 = Estimated Fare
Points Earned = Actual Fare (in rupees)
```

#### Complete Booking
1. Click "Complete Booking" on IN_PROGRESS booking
2. Enter actual fare amount
3. Loyalty points awarded automatically
4. Booking status changes to COMPLETED

### Statistics Dashboard
- Total Bookings
- Completed Bookings
- Total Revenue
- Total Distance Covered

### API Endpoints

```bash
# Get all bookings
curl http://localhost:8080/api/bookings

# Create booking
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleId": 1,
    "customerId": 1,
    "pickupLatitude": 28.7041,
    "pickupLongitude": 77.1025,
    "dropoffLatitude": 28.6139,
    "dropoffLongitude": 77.2090,
    "pickupTime": "2024-03-07T10:00:00",
    "distanceKm": 15
  }'

# Complete booking
curl -X PUT http://localhost:8080/api/bookings/1/complete \
  -H "Content-Type: application/json" \
  -d '{
    "actualFare": 225
  }'

# Get loyalty points
curl http://localhost:8080/api/bookings/loyalty/1

# Redeem points
curl -X PUT http://localhost:8080/api/bookings/loyalty/1/redeem/100
```

---

## 📊 Module 6: Admin Reports & Analytics

### How to Access
1. Login as **Admin** (admin@neurofleetx.com / Admin@123)
2. Navigate to **Analytics** link in navbar (or `/analytics`)

### Key Features

#### Date Range Selection
- Select custom start and end dates
- Default: Last 30 days
- Click "🔄 Refresh" to update dashboard

#### Overview Tab
Shows comprehensive dashboard:
- **Key Performance Indicators**:
  - Total Bookings
  - Completed Bookings
  - Cancelled Bookings
  - Completion Rate %
  - Total Revenue
  - Active Vehicles
  
- **Customer Insights**:
  - Total Customers
  - Total Spending
  - Avg Spending per Customer

- **Last 7 Days Performance**:
  - Daily table with bookings, revenue, distance, utilization

#### Operational Tab
Shows operational metrics:
- Total Bookings
- Completed/Pending/Cancelled counts
- Completion Rate
- Fleet statistics

#### Revenue Tab
Shows revenue analytics for selected date range:
- Total Revenue
- Completed Bookings
- Avg Fare per Booking
- Total Distance

#### Customer Tab
Shows customer analytics:
- Total Customers
- Total Spending
- Avg Spending per Customer

#### Vehicles Tab
Shows vehicle utilization metrics:
- Total Vehicles
- Total Bookings
- Avg Bookings per Vehicle
- Detailed per-vehicle breakdown

### Key Metrics Explained

**Completion Rate %**:
```
(Completed Bookings / Total Bookings) × 100
```

**Avg Spending per Customer**:
```
Total Spending / Total Customers
```

**Avg Fare per Booking**:
```
Total Revenue / Completed Bookings
```

**Vehicle Utilization**:
```
(Active Vehicles / Total Vehicles) × 100
```

### API Endpoints

```bash
# Get full dashboard report
curl http://localhost:8080/api/analytics/reports/dashboard

# Get operational metrics
curl http://localhost:8080/api/analytics/reports/operational

# Get revenue analytics (date range)
curl 'http://localhost:8080/api/analytics/reports/revenue?startDate=2024-02-06&endDate=2024-03-07'

# Get customer analytics
curl http://localhost:8080/api/analytics/reports/customer

# Get vehicle utilization
curl http://localhost:8080/api/analytics/reports/vehicle-utilization

# Generate daily analytics
curl -X POST http://localhost:8080/api/analytics/daily/2024-03-07
```

---

## 🧪 Testing Workflow

### Complete Workflow Test

1. **Create a Maintenance Record**:
   - Login as admin
   - Go to `/maintenance`
   - Create record for vehicle ID 1
   - Verify it appears in list

2. **Create a Booking**:
   - Login as demo user
   - Go to `/bookings`
   - Create booking with:
     - Vehicle ID: 1
     - Distance: 15 km
     - Estimated Fare: ₹225
   - Verify booking appears

3. **Complete the Booking**:
   - Click "Complete Booking"
   - Enter actual fare: ₹225
   - Verify loyalty points added (225 points)

4. **Check Analytics**:
   - Login as admin
   - Go to `/analytics`
   - Verify:
     - 1 completed booking
     - ₹225 revenue
     - 15 km distance
     - Loyalty points shown

---

## 🔧 Troubleshooting

### Backend Issues

**Tables not created**:
```bash
# Verify schema loaded
mysql -u root -p neurofleetx -e "SHOW TABLES;"

# Should see: maintenance_records, bookings, loyalty_points, daily_analytics
```

**Port conflicts**:
```bash
# Backend default: 8080
# Kill existing: lsof -ti:8080 | xargs kill -9
```

### Frontend Issues

**"Cannot find route"**:
- Ensure backend is running
- Check Navigation.jsx has all routes
- Clear browser cache: Ctrl+Shift+Delete

**API 404 errors**:
- Verify backend endpoints exist
- Check CORS config in backend
- Test endpoints with curl first

**Missing styles**:
- Ensure all CSS files imported in App.jsx
- Check `/styles` folder has all files

---

## 📝 Sample Data for Testing

### Create Test Bookings

```bash
# Vehicle 1, Customer 1
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleId": 1,
    "customerId": 1,
    "pickupLatitude": 28.7041,
    "pickupLongitude": 77.1025,
    "dropoffLatitude": 28.6139,
    "dropoffLongitude": 77.2090,
    "pickupTime": "2024-03-07T14:00:00",
    "distanceKm": 20
  }'

# Vehicle 2, Customer 2
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleId": 2,
    "customerId": 2,
    "pickupLatitude": 28.6400,
    "pickupLongitude": 77.1900,
    "dropoffLatitude": 28.5355,
    "dropoffLongitude": 77.3910,
    "pickupTime": "2024-03-07T15:00:00",
    "distanceKm": 25
  }'
```

### Create Test Maintenance

```bash
curl -X POST http://localhost:8080/api/maintenance/records \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleId": 3,
    "maintenanceType": "Engine Inspection",
    "description": "Complete engine diagnostics",
    "scheduledDate": "2024-03-15",
    "cost": 5000
  }'
```

---

## 📚 Documentation Files

- **MODULES_1_6_COMPLETE.md** - Complete project documentation
- **PROJECT_DOCUMENTATION.md** - Frontend structure and design
- **README.md** - Main project overview
- **QUICK_START.md** - Frontend quick start

---

## 🎉 Success Checklist

- [ ] Backend running on 8080
- [ ] Frontend running on 5173
- [ ] Database tables created
- [ ] Can login as admin
- [ ] Can create maintenance record
- [ ] Can create booking
- [ ] Can complete booking
- [ ] Loyalty points working
- [ ] Analytics dashboard loads
- [ ] All reports visible

---

## 📞 Support

For issues, check:
1. Backend logs: `mvn spring-boot:run` output
2. Browser console: F12 → Console tab
3. Network tab for API errors
4. Database: `mysql -u root -p neurofleetx -e "SELECT COUNT(*) FROM bookings;"`

**All modules are production-ready!** 🚀
