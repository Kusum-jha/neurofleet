import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useEffect } from 'react';
import Navigation from './components/Navigation';
import ProtectedRoute from './components/ProtectedRoute';
import RoleRoute from './components/RoleRoute';
import Login from './pages/Login';
import Register from './pages/Register';
import Home from './pages/Home';
import Profile from './pages/Profile';
import AdminDashboard from './pages/AdminDashboard';
import ManagerDashboard from './pages/ManagerDashboard';
import DriverDashboard from './pages/DriverDashboard';
import CustomerDashboard from './pages/CustomerDashboard';
import FleetInventory from './pages/FleetInventory';
import RoutePlanner from './pages/RoutePlanner';
import MaintenancePage from './pages/MaintenancePage';
import BookingsPage from './pages/BookingsPage';
import AnalyticsPage from './pages/AnalyticsPage';
import { createDemoUsers, getCurrentUser, loginUser } from './utils/auth';
import './styles/theme.css';
import './styles/pages.css';
import './styles/components.css';
import './styles/dashboard.css';
import './styles/vehicles.css';
import './styles/routes.css';
import './App.css';

function App() {
  useEffect(() => {
    // Initialize demo users on first load
    createDemoUsers();
    
    // Auto-login as admin if not already logged in
    if (!getCurrentUser()) {
      loginUser('admin@neurofleetx.com', 'Admin@123');
    }
  }, []);

  return (
    <Router>
      <Navigation />
      <Routes>
        {/* Public Routes */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Protected Routes */}
        <Route
          path="/home"
          element={
            <ProtectedRoute>
              <Home />
            </ProtectedRoute>
          }
        />
        <Route
          path="/profile"
          element={
            <ProtectedRoute>
              <Profile />
            </ProtectedRoute>
          }
        />

        {/* Role-Based Dashboards */}
        <Route
          path="/admin"
          element={
            <ProtectedRoute>
              <RoleRoute requiredRole="ADMIN">
                <AdminDashboard />
              </RoleRoute>
            </ProtectedRoute>
          }
        />
        <Route
          path="/manager"
          element={
            <ProtectedRoute>
              <RoleRoute requiredRole="FLEET_MANAGER">
                <ManagerDashboard />
              </RoleRoute>
            </ProtectedRoute>
          }
        />
        <Route
          path="/driver"
          element={
            <ProtectedRoute>
              <RoleRoute requiredRole="DRIVER">
                <DriverDashboard />
              </RoleRoute>
            </ProtectedRoute>
          }
        />
        <Route
          path="/customer"
          element={
            <ProtectedRoute>
              <RoleRoute requiredRole="CUSTOMER">
                <CustomerDashboard />
              </RoleRoute>
            </ProtectedRoute>
          }
        />

        {/* Module 2: Fleet Inventory */}
        <Route
          path="/fleet"
          element={
            <ProtectedRoute>
              <FleetInventory />
            </ProtectedRoute>
          }
        />

        {/* Module 3: Route Planner */}
        <Route
          path="/routes"
          element={
            <ProtectedRoute>
              <RoutePlanner />
            </ProtectedRoute>
          }
        />

        {/* Module 4: Predictive Maintenance */}
        <Route
          path="/maintenance"
          element={
            <ProtectedRoute>
              <RoleRoute requiredRole="ADMIN">
                <MaintenancePage />
              </RoleRoute>
            </ProtectedRoute>
          }
        />

        {/* Module 5: Customer Bookings */}
        <Route
          path="/bookings"
          element={
            <ProtectedRoute>
              <BookingsPage />
            </ProtectedRoute>
          }
        />

        {/* Module 6: Admin Analytics & Reports */}
        <Route
          path="/analytics"
          element={
            <ProtectedRoute>
              <RoleRoute requiredRole="ADMIN">
                <AnalyticsPage />
              </RoleRoute>
            </ProtectedRoute>
          }
        />

        {/* Default Route */}
        <Route path="/" element={<Navigate to="/admin" />} />
        <Route path="*" element={<Navigate to="/admin" />} />
      </Routes>
    </Router>
  );
}

export default App;
