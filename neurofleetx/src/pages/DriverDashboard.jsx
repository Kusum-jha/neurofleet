import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import DashboardMetrics from '../components/DashboardMetrics';
import { dashboardService } from '../services/dashboardService';
import { authService } from '../services/authService';
import '../styles/dashboard.css';

const DriverDashboard = () => {
  const [metrics, setMetrics] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();
  const user = authService.getCurrentUser();

  useEffect(() => {
    const fetchMetrics = async () => {
      setIsLoading(true);
      const data = await dashboardService.getDriverMetrics();
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
        <h1>Driver Dashboard</h1>
        <p>Your Daily Performance & Earnings</p>
        {user && <span className="role-badge">{user.role}</span>}
      </div>

      <div className="dashboard-actions">
        <button className="action-btn primary" onClick={() => navigate('/routes')}>
          🗺️ My Routes
        </button>
        <button className="action-btn secondary" onClick={() => navigate('/profile')}>
          👤 Profile
        </button>
      </div>

      <div className="dashboard-section">
        <h2 className="section-title">Performance Metrics</h2>
        <DashboardMetrics metrics={metrics} />
      </div>

      <div className="dashboard-section">
        <h2 className="section-title">Today's Summary</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
          <div className="metric-card glass-effect">
            <div style={{ fontSize: '2rem' }}>💰</div>
            <div>
              <p className="metric-label">Earnings</p>
              <p className="metric-value">₹{metrics.earnings || 0}</p>
            </div>
          </div>
          <div className="metric-card glass-effect">
            <div style={{ fontSize: '2rem' }}>✅</div>
            <div>
              <p className="metric-label">Completed Routes</p>
              <p className="metric-value">{metrics.completedRoutes || 0}</p>
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
            <div style={{ fontSize: '2rem' }}>⭐</div>
            <div>
              <p className="metric-label">Rating</p>
              <p className="metric-value">{metrics.averageRating || 0}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DriverDashboard;
