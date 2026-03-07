import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import DashboardMetrics from '../components/DashboardMetrics';
import { dashboardService } from '../services/dashboardService';
import { authService } from '../services/authService';
import '../styles/dashboard.css';

const AdminDashboard = () => {
  const [metrics, setMetrics] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();
  const user = authService.getCurrentUser();

  useEffect(() => {
    const fetchMetrics = async () => {
      setIsLoading(true);
      const data = await dashboardService.getAdminMetrics();
      setMetrics(data);
      setIsLoading(false);
    };

    fetchMetrics();
    const interval = setInterval(fetchMetrics, 10000);
    return () => clearInterval(interval);
  }, []);

  if (isLoading) {
    return <div className="dashboard-container">Loading dashboard...</div>;
  }

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1>Admin Dashboard</h1>
        <p>System Overview & Management</p>
        {user && <span className="role-badge">{user.role}</span>}
      </div>

      <div className="dashboard-actions">
        <button className="action-btn primary" onClick={() => navigate('/fleet')}>
          🚗 Manage Fleet
        </button>
        <button className="action-btn primary" onClick={() => navigate('/routes')}>
          🗺️ Manage Routes
        </button>
        <button className="action-btn secondary" onClick={() => navigate('/profile')}>
          👤 Profile
        </button>
      </div>

      <div className="dashboard-section">
        <h2 className="section-title">System Metrics</h2>
        <DashboardMetrics metrics={metrics} />
      </div>

      <div className="dashboard-section">
        <h2 className="section-title">Quick Stats</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
          <div className="metric-card glass-effect">
            <div style={{ fontSize: '2rem' }}>👥</div>
            <div>
              <p className="metric-label">Total Users</p>
              <p className="metric-value">{metrics.totalUsers || 0}</p>
            </div>
          </div>
          <div className="metric-card glass-effect">
            <div style={{ fontSize: '2rem' }}>🚗</div>
            <div>
              <p className="metric-label">Total Vehicles</p>
              <p className="metric-value">{metrics.totalVehicles || 0}</p>
            </div>
          </div>
          <div className="metric-card glass-effect">
            <div style={{ fontSize: '2rem' }}>✅</div>
            <div>
              <p className="metric-label">Available</p>
              <p className="metric-value">{metrics.availableVehicles || 0}</p>
            </div>
          </div>
          <div className="metric-card glass-effect">
            <div style={{ fontSize: '2rem' }}>🔄</div>
            <div>
              <p className="metric-label">In Use</p>
              <p className="metric-value">{metrics.busyVehicles || 0}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
