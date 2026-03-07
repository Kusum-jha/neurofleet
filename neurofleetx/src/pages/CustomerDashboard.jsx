import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import DashboardMetrics from '../components/DashboardMetrics';
import { dashboardService } from '../services/dashboardService';
import { authService } from '../services/authService';
import '../styles/dashboard.css';

const CustomerDashboard = () => {
  const [metrics, setMetrics] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();
  const user = authService.getCurrentUser();

  useEffect(() => {
    const fetchMetrics = async () => {
      setIsLoading(true);
      const data = await dashboardService.getCustomerMetrics();
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
        <h1>Customer Dashboard</h1>
        <p>Your Bookings & Rewards</p>
        {user && <span className="role-badge">{user.role}</span>}
      </div>

      <div className="dashboard-actions">
        <button className="action-btn primary" onClick={() => navigate('/fleet')}>
          🚗 Book a Vehicle
        </button>
        <button className="action-btn secondary" onClick={() => navigate('/profile')}>
          👤 Profile
        </button>
      </div>

      <div className="dashboard-section">
        <h2 className="section-title">Your Account</h2>
        <DashboardMetrics metrics={metrics} />
      </div>

      <div className="dashboard-section">
        <h2 className="section-title">Quick Stats</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
          <div className="metric-card glass-effect">
            <div style={{ fontSize: '2rem' }}>📅</div>
            <div>
              <p className="metric-label">Upcoming Bookings</p>
              <p className="metric-value">{metrics.upcomingBookings || 0}</p>
            </div>
          </div>
          <div className="metric-card glass-effect">
            <div style={{ fontSize: '2rem' }}>✅</div>
            <div>
              <p className="metric-label">Completed Bookings</p>
              <p className="metric-value">{metrics.completedBookings || 0}</p>
            </div>
          </div>
          <div className="metric-card glass-effect">
            <div style={{ fontSize: '2rem' }}>💳</div>
            <div>
              <p className="metric-label">Total Spent</p>
              <p className="metric-value">₹{metrics.totalSpent || 0}</p>
            </div>
          </div>
          <div className="metric-card glass-effect">
            <div style={{ fontSize: '2rem' }}>🏆</div>
            <div>
              <p className="metric-label">Loyalty Points</p>
              <p className="metric-value">{metrics.loyaltyPoints || 0}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CustomerDashboard;
