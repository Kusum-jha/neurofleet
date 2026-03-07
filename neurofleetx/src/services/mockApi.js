// Mock API for demo purposes (when backend is not available)

const MOCK_TOKEN = 'mock-jwt-token-' + Date.now();
const MOCK_USERS = {
  'admin@neurofleetx.com': { id: 1, username: 'Admin User', role: 'ADMIN' },
  'manager@neurofleetx.com': { id: 2, username: 'Fleet Manager', role: 'FLEET_MANAGER' },
  'driver@neurofleetx.com': { id: 3, username: 'Driver', role: 'DRIVER' },
  'customer@neurofleetx.com': { id: 4, username: 'Customer', role: 'CUSTOMER' },
};

const MOCK_VEHICLES = [
  {
    id: 1,
    vehicleNumber: 'NF-EV-001',
    status: 'AVAILABLE',
    vehicleType: 'Electric',
    capacity: 5,
    fuelType: 'Electric',
    batteryPercentage: 85,
    fuelPercentage: null,
    currentLatitude: 28.6139,
    currentLongitude: 77.209,
    speedKmh: 0,
    mileageKm: 15000,
    lastMaintenance: '2024-01-15',
  },
  {
    id: 2,
    vehicleNumber: 'NF-EV-002',
    status: 'IN_USE',
    vehicleType: 'Electric',
    capacity: 5,
    fuelType: 'Electric',
    batteryPercentage: 60,
    fuelPercentage: null,
    currentLatitude: 28.5355,
    currentLongitude: 77.391,
    speedKmh: 45,
    mileageKm: 18000,
    lastMaintenance: '2023-12-20',
  },
  {
    id: 3,
    vehicleNumber: 'NF-GAS-001',
    status: 'AVAILABLE',
    vehicleType: 'Sedan',
    capacity: 5,
    fuelType: 'Petrol',
    batteryPercentage: null,
    fuelPercentage: 90,
    currentLatitude: 28.5921,
    currentLongitude: 77.046,
    speedKmh: 0,
    mileageKm: 25000,
    lastMaintenance: '2024-02-01',
  },
  {
    id: 4,
    vehicleNumber: 'NF-GAS-002',
    status: 'MAINTENANCE',
    vehicleType: 'SUV',
    capacity: 7,
    fuelType: 'Petrol',
    batteryPercentage: null,
    fuelPercentage: 45,
    currentLatitude: 28.7041,
    currentLongitude: 77.1025,
    speedKmh: 0,
    mileageKm: 32000,
    lastMaintenance: '2024-01-10',
  },
  {
    id: 5,
    vehicleNumber: 'NF-TRUCK-001',
    status: 'IN_USE',
    vehicleType: 'Truck',
    capacity: 15,
    fuelType: 'Diesel',
    batteryPercentage: null,
    fuelPercentage: 75,
    currentLatitude: 28.4595,
    currentLongitude: 77.0266,
    speedKmh: 55,
    mileageKm: 45000,
    lastMaintenance: '2024-02-05',
  },
  {
    id: 6,
    vehicleNumber: 'NF-AUTO-001',
    status: 'AVAILABLE',
    vehicleType: 'Auto Rickshaw',
    capacity: 3,
    fuelType: 'CNG',
    batteryPercentage: null,
    fuelPercentage: 80,
    currentLatitude: 28.6162,
    currentLongitude: 77.231,
    speedKmh: 0,
    mileageKm: 12000,
    lastMaintenance: '2024-01-25',
  },
];

export const mockAuthService = {
  login: async (email, password, role) => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const user = MOCK_USERS[email];
        if (user) {
          // For demo accounts, accept any password
          const token = 'mock-jwt-token-' + Date.now();
          localStorage.setItem('neurofleetx_token', token);
          localStorage.setItem('neurofleetx_user', JSON.stringify({
            id: user.id,
            username: user.username,
            email: email,
            role: user.role,
          }));
          resolve({
            success: true,
            data: {
              token: token,
              userId: user.id,
              username: user.username,
              email: email,
              role: user.role,
            },
            message: 'Login successful',
          });
        } else {
          // For locally registered users, check password
          const users = JSON.parse(localStorage.getItem('neurofleetx_users') || '[]');
          const localUser = users.find(u => u.email === email && u.password === password);
          if (localUser) {
            const token = 'local-token-' + Date.now();
            localStorage.setItem('neurofleetx_token', token);
            localStorage.setItem('neurofleetx_user', JSON.stringify({
              id: localUser.id,
              username: localUser.username,
              email: email,
              role: localUser.role || role,
            }));
            resolve({
              success: true,
              data: {
                token: token,
                userId: localUser.id,
                username: localUser.username,
                email: email,
                role: localUser.role || role,
              },
              message: 'Login successful',
            });
          } else {
            resolve({
              success: false,
              message: 'User not found. Try admin@neurofleetx.com or create an account',
            });
          }
        }
      }, 500);
    });
  },
};

export const mockVehicleService = {
  getAllVehicles: async () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(MOCK_VEHICLES);
      }, 300);
    });
  },

  simulateTelemetry: async (vehicleId) => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const vehicle = MOCK_VEHICLES.find(v => v.id === vehicleId);
        if (vehicle) {
          vehicle.speedKmh = Math.random() * 80;
          if (vehicle.batteryPercentage) {
            vehicle.batteryPercentage = Math.max(10, vehicle.batteryPercentage - Math.random() * 5);
          }
          if (vehicle.fuelPercentage) {
            vehicle.fuelPercentage = Math.max(10, vehicle.fuelPercentage - Math.random() * 3);
          }
          vehicle.currentLatitude += (Math.random() - 0.5) * 0.01;
          vehicle.currentLongitude += (Math.random() - 0.5) * 0.01;
        }
        resolve(vehicle);
      }, 300);
    });
  },
};

export const mockDashboardService = {
  getAdminMetrics: async () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          totalUsers: 1250,
          totalVehicles: 6,
          totalRoutes: 384,
          activeRoutes: 12,
          completedRoutes: 372,
          availableVehicles: 3,
          busyVehicles: 2,
          maintenanceVehicles: 1,
        });
      }, 300);
    });
  },

  getManagerMetrics: async () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          totalVehicles: 6,
          availableVehicles: 3,
          activeRoutes: 12,
          plannedRoutes: 24,
          completedToday: 18,
          maintenanceVehicles: 1,
          fleetUtilization: 50,
        });
      }, 300);
    });
  },

  getDriverMetrics: async () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          assignedRoutes: 28,
          completedRoutes: 24,
          activeRoutes: 4,
          earnings: 2500,
          averageRating: 4.8,
          tripsToday: 8,
        });
      }, 300);
    });
  },

  getCustomerMetrics: async () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          upcomingBookings: 3,
          completedBookings: 12,
          totalSpent: 4500,
          averageRideTime: 45,
          favoriteVehicleType: 'Electric',
          loyaltyPoints: 1250,
        });
      }, 300);
    });
  },
};

export const mockRouteService = {
  optimizeRoute: async (vehicleId, startLat, startLng, endLat, endLng) => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const distance = Math.sqrt(
          Math.pow(parseFloat(endLat) - parseFloat(startLat), 2) +
          Math.pow(parseFloat(endLng) - parseFloat(startLng), 2)
        ) * 111; // rough km conversion

        resolve([
          {
            id: 1,
            vehicleId: vehicleId,
            vehicleNumber: 'NF-EV-001',
            startLatitude: parseFloat(startLat),
            startLongitude: parseFloat(startLng),
            endLatitude: parseFloat(endLat),
            endLongitude: parseFloat(endLng),
            distanceKm: distance,
            estimatedDurationMinutes: Math.round((distance / 50) * 60),
            status: 'PLANNED',
          },
          {
            id: 2,
            vehicleId: vehicleId,
            vehicleNumber: 'NF-EV-001',
            startLatitude: parseFloat(startLat),
            startLongitude: parseFloat(startLng),
            endLatitude: parseFloat(endLat),
            endLongitude: parseFloat(endLng),
            distanceKm: distance * 1.1,
            estimatedDurationMinutes: Math.round((distance * 1.1 / 70) * 60),
            status: 'PLANNED',
          },
          {
            id: 3,
            vehicleId: vehicleId,
            vehicleNumber: 'NF-EV-001',
            startLatitude: parseFloat(startLat),
            startLongitude: parseFloat(startLng),
            endLatitude: parseFloat(endLat),
            endLongitude: parseFloat(endLng),
            distanceKm: distance * 1.05,
            estimatedDurationMinutes: Math.round((distance * 1.05 / 45) * 60),
            status: 'PLANNED',
          },
        ]);
      }, 500);
    });
  },
};
