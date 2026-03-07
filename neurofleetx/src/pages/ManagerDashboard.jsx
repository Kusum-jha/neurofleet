import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import DashboardMetrics from '../components/DashboardMetrics';
import { dashboardService } from '../services/dashboardService';
import { authService } from '../services/authService';
import '../styles/dashboard.css';

const ManagerDashboard = () => {
  const [metrics, setMetrics] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();
  const user = authService.getCurrentUser();

  useEffect(() => {
    const fetchMetrics = async () => {
      setIsLoading(true);
      const data = await dashboardService.getManagerMetrics();
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
        <h1>Fleet Manager Dashboard</h1>
        <p>Fleet Operations & Management</p>
        {user && <span className="role-badge">{user.role.replace('_', ' ')}</span>}
      </div>

      <div className="dashboard-actions">
        <button className="action-btn primary" onClick={() => navigate('/fleet')}>
          🚗 View Fleet
        </button>
        <button className="action-btn primary" onClick={() => navigate('/routes')}>
          🗺️ View Routes
        </button>
        <button className="action-btn secondary" onClick={() => navigate('/profile')}>
          👤 Profile
        </button>
      </div>

      <div className="dashboard-section">
        <h2 className="section-title">Fleet Metrics</h2>
        <DashboardMetrics metrics={metrics} />
      </div>

      <div className="dashboard-section">
        <h2 className="section-title">Fleet Status Overview</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
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
              <p className="metric-label">Active Routes</p>
              <p className="metric-value">{metrics.activeRoutes || 0}</p>
            </div>
          </div>
          <div className="metric-card glass-effect">
            <div style={{ fontSize: '2rem' }}>📊</div>
            <div>
              <p className="metric-label">Utilization</p>
              <p className="metric-value">{metrics.fleetUtilization?.toFixed(1) || 0}%</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ManagerDashboard;
