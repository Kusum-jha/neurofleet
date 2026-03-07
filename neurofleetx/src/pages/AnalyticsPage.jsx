import React, { useState, useEffect } from 'react';
import Navigation from '../components/Navigation';
import axios from 'axios';
import '../styles/dashboard.css';
import '../styles/pages.css';

const AnalyticsPage = () => {
  const [dashboardData, setDashboardData] = useState(null);
  const [operationalMetrics, setOperationalMetrics] = useState(null);
  const [customerAnalytics, setCustomerAnalytics] = useState(null);
  const [vehicleUtilization, setVehicleUtilization] = useState(null);
  const [revenueData, setRevenueData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('overview');
  const [dateRange, setDateRange] = useState({
    startDate: new Date(new Date().setDate(new Date().getDate() - 30)).toISOString().split('T')[0],
    endDate: new Date().toISOString().split('T')[0]
  });

  useEffect(() => {
    fetchAnalyticsData();
  }, []);

  const fetchAnalyticsData = async () => {
    setLoading(true);
    try {
      const [dashRes, opRes, custRes, vehicRes, revRes] = await Promise.all([
        axios.get('http://localhost:8080/api/analytics/reports/dashboard'),
        axios.get('http://localhost:8080/api/analytics/reports/operational'),
        axios.get('http://localhost:8080/api/analytics/reports/customer'),
        axios.get('http://localhost:8080/api/analytics/reports/vehicle-utilization'),
        axios.get(
          `http://localhost:8080/api/analytics/reports/revenue?startDate=${dateRange.startDate}&endDate=${dateRange.endDate}`
        )
      ]);

      setDashboardData(dashRes.data.data);
      setOperationalMetrics(opRes.data.data);
      setCustomerAnalytics(custRes.data.data);
      setVehicleUtilization(vehicRes.data.data);
      setRevenueData(revRes.data.data);
    } catch (error) {
      console.error('Error fetching analytics:', error);
    } finally {
      setLoading(false);
    }
  };

  const MetricCard = ({ title, value, subtitle, icon }) => (
    <div className="metric-card glass-card">
      <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem' }}>{icon}</div>
      <div className="metric-value">{value}</div>
      <div className="metric-label">{title}</div>
      {subtitle && <small style={{ color: '#9099a8', marginTop: '0.25rem', display: 'block' }}>{subtitle}</small>}
    </div>
  );

  return (
    <div className="dashboard-container">
      <Navigation />
      
      <div className="dashboard-header">
        <h1>📊 Admin Reports & Analytics Module</h1>
        <p>Comprehensive platform analytics and performance metrics</p>
      </div>

      {/* Date Range Selector */}
      <div className="glass-card" style={{ marginBottom: '2rem', padding: '1.5rem', display: 'flex', gap: '1rem', alignItems: 'flex-end' }}>
        <div>
          <label className="bubble-label" style={{ display: 'block', marginBottom: '0.5rem' }}>From</label>
          <input
            type="date"
            className="bubble-input"
            value={dateRange.startDate}
            onChange={(e) => setDateRange({ ...dateRange, startDate: e.target.value })}
          />
        </div>
        <div>
          <label className="bubble-label" style={{ display: 'block', marginBottom: '0.5rem' }}>To</label>
          <input
            type="date"
            className="bubble-input"
            value={dateRange.endDate}
            onChange={(e) => setDateRange({ ...dateRange, endDate: e.target.value })}
          />
        </div>
        <button className="bubble-btn" onClick={fetchAnalyticsData}>
          🔄 Refresh
        </button>
      </div>

      {/* Tab Navigation */}
      <div className="tab-buttons" style={{ marginBottom: '2rem' }}>
        <button
          className={`bubble-btn ${activeTab === 'overview' ? 'active' : ''}`}
          onClick={() => setActiveTab('overview')}
        >
          📈 Overview
        </button>
        <button
          className={`bubble-btn ${activeTab === 'operational' ? 'active' : ''}`}
          onClick={() => setActiveTab('operational')}
        >
          ⚙️ Operations
        </button>
        <button
          className={`bubble-btn ${activeTab === 'revenue' ? 'active' : ''}`}
          onClick={() => setActiveTab('revenue')}
        >
          💰 Revenue
        </button>
        <button
          className={`bubble-btn ${activeTab === 'customers' ? 'active' : ''}`}
          onClick={() => setActiveTab('customers')}
        >
          👥 Customers
        </button>
        <button
          className={`bubble-btn ${activeTab === 'vehicles' ? 'active' : ''}`}
          onClick={() => setActiveTab('vehicles')}
        >
          🚗 Vehicles
        </button>
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '3rem', color: '#4da6ff' }}>
          <p>Loading analytics...</p>
        </div>
      ) : (
        <>
          {/* Overview Tab */}
          {activeTab === 'overview' && dashboardData && (
            <>
              {/* Operational Metrics */}
              <div style={{ marginBottom: '2rem' }}>
                <h2 style={{ marginBottom: '1.5rem' }}>🎯 Key Performance Indicators</h2>
                <div className="metrics-grid">
                  <MetricCard
                    icon="📋"
                    title="Total Bookings"
                    value={dashboardData.operationalMetrics.totalBookings}
                  />
                  <MetricCard
                    icon="✅"
                    title="Completed"
                    value={dashboardData.operationalMetrics.completedBookings}
                  />
                  <MetricCard
                    icon="❌"
                    title="Cancelled"
                    value={dashboardData.operationalMetrics.cancelledBookings}
                  />
                  <MetricCard
                    icon="📊"
                    title="Completion Rate"
                    value={`${dashboardData.operationalMetrics.completionRate.toFixed(1)}%`}
                  />
                  <MetricCard
                    icon="💰"
                    title="Total Revenue"
                    value={`₹${dashboardData.operationalMetrics.totalRevenue}`}
                  />
                  <MetricCard
                    icon="🚗"
                    title="Active Vehicles"
                    value={dashboardData.operationalMetrics.activeVehicles}
                    subtitle={`of ${dashboardData.operationalMetrics.totalVehicles}`}
                  />
                </div>
              </div>

              {/* Customer Metrics */}
              <div style={{ marginBottom: '2rem' }}>
                <h2 style={{ marginBottom: '1.5rem' }}>👥 Customer Insights</h2>
                <div className="metrics-grid">
                  <MetricCard
                    icon="👤"
                    title="Total Customers"
                    value={dashboardData.customerAnalytics.totalCustomers}
                  />
                  <MetricCard
                    icon="💵"
                    title="Total Spending"
                    value={`₹${dashboardData.customerAnalytics.totalSpending}`}
                  />
                  <MetricCard
                    icon="📈"
                    title="Avg Spending/Customer"
                    value={`₹${dashboardData.customerAnalytics.avgSpendingPerCustomer}`}
                  />
                </div>
              </div>

              {/* Recent Daily Data */}
              {dashboardData.recentDailyData && (
                <div>
                  <h2 style={{ marginBottom: '1.5rem' }}>📅 Last 7 Days Performance</h2>
                  <div className="glass-card" style={{ padding: '1.5rem', overflowX: 'auto' }}>
                    <table className="data-table" style={{ width: '100%', borderCollapse: 'collapse' }}>
                      <thead>
                        <tr style={{ borderBottom: '2px solid rgba(0, 102, 204, 0.3)' }}>
                          <th style={{ padding: '1rem', textAlign: 'left' }}>Date</th>
                          <th style={{ padding: '1rem', textAlign: 'left' }}>Bookings</th>
                          <th style={{ padding: '1rem', textAlign: 'left' }}>Completed</th>
                          <th style={{ padding: '1rem', textAlign: 'left' }}>Revenue</th>
                          <th style={{ padding: '1rem', textAlign: 'left' }}>Distance</th>
                          <th style={{ padding: '1rem', textAlign: 'left' }}>Avg Util.</th>
                        </tr>
                      </thead>
                      <tbody>
                        {dashboardData.recentDailyData.map((day, idx) => (
                          <tr key={idx} style={{ borderBottom: '1px solid rgba(0, 102, 204, 0.1)' }}>
                            <td style={{ padding: '1rem' }}>{day.analyticsDate}</td>
                            <td style={{ padding: '1rem' }}>{day.totalBookings}</td>
                            <td style={{ padding: '1rem' }}>{day.completedBookings}</td>
                            <td style={{ padding: '1rem' }}>₹{day.totalRevenue}</td>
                            <td style={{ padding: '1rem' }}>{day.totalDistanceKm} km</td>
                            <td style={{ padding: '1rem' }}>{day.avgVehicleUtilization}%</td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              )}
            </>
          )}

          {/* Operational Tab */}
          {activeTab === 'operational' && operationalMetrics && (
            <div className="glass-card" style={{ padding: '2rem' }}>
              <h2 style={{ marginBottom: '2rem' }}>Operational Metrics</h2>
              <div className="metrics-grid">
                <MetricCard
                  icon="📋"
                  title="Total Bookings"
                  value={operationalMetrics.totalBookings}
                />
                <MetricCard
                  icon="✅"
                  title="Completed Bookings"
                  value={operationalMetrics.completedBookings}
                />
                <MetricCard
                  icon="⏳"
                  title="Pending Bookings"
                  value={operationalMetrics.pendingBookings}
                />
                <MetricCard
                  icon="❌"
                  title="Cancelled Bookings"
                  value={operationalMetrics.cancelledBookings}
                />
                <MetricCard
                  icon="📊"
                  title="Completion Rate"
                  value={`${operationalMetrics.completionRate.toFixed(1)}%`}
                />
                <MetricCard
                  icon="🚗"
                  title="Fleet Size"
                  value={operationalMetrics.totalVehicles}
                  subtitle={`${operationalMetrics.activeVehicles} active`}
                />
              </div>
            </div>
          )}

          {/* Revenue Tab */}
          {activeTab === 'revenue' && revenueData && (
            <div className="glass-card" style={{ padding: '2rem' }}>
              <h2 style={{ marginBottom: '2rem' }}>Revenue Analytics</h2>
              <div className="metrics-grid">
                <MetricCard
                  icon="💰"
                  title="Total Revenue"
                  value={`₹${revenueData.totalRevenue}`}
                />
                <MetricCard
                  icon="✅"
                  title="Completed Bookings"
                  value={revenueData.completedBookings}
                />
                <MetricCard
                  icon="📈"
                  title="Avg Fare/Booking"
                  value={`₹${revenueData.avgFarePerBooking}`}
                />
                <MetricCard
                  icon="📍"
                  title="Total Distance"
                  value={`${revenueData.totalDistance} km`}
                />
              </div>
            </div>
          )}

          {/* Customers Tab */}
          {activeTab === 'customers' && customerAnalytics && (
            <div className="glass-card" style={{ padding: '2rem' }}>
              <h2 style={{ marginBottom: '2rem' }}>Customer Analytics</h2>
              <div className="metrics-grid">
                <MetricCard
                  icon="👥"
                  title="Total Customers"
                  value={customerAnalytics.totalCustomers}
                />
                <MetricCard
                  icon="💵"
                  title="Total Spending"
                  value={`₹${customerAnalytics.totalSpending}`}
                />
                <MetricCard
                  icon="📊"
                  title="Avg Spending/Customer"
                  value={`₹${customerAnalytics.avgSpendingPerCustomer}`}
                />
              </div>
            </div>
          )}

          {/* Vehicles Tab */}
          {activeTab === 'vehicles' && vehicleUtilization && (
            <div className="glass-card" style={{ padding: '2rem' }}>
              <h2 style={{ marginBottom: '2rem' }}>Vehicle Utilization</h2>
              <div className="metrics-grid">
                <MetricCard
                  icon="🚗"
                  title="Total Vehicles"
                  value={vehicleUtilization.totalVehicles}
                />
                <MetricCard
                  icon="📋"
                  title="Total Bookings"
                  value={vehicleUtilization.totalBookings}
                />
                <MetricCard
                  icon="📊"
                  title="Avg Bookings/Vehicle"
                  value={vehicleUtilization.avgBookingsPerVehicle.toFixed(2)}
                />
              </div>

              {vehicleUtilization.vehicleBookingDetails && (
                <div style={{ marginTop: '2rem' }}>
                  <h3 style={{ marginBottom: '1rem' }}>Vehicle Booking Details</h3>
                  <div className="metrics-grid">
                    {Object.entries(vehicleUtilization.vehicleBookingDetails).slice(0, 6).map(([vehicleId, bookings]) => (
                      <MetricCard
                        key={vehicleId}
                        icon="📋"
                        title={`Vehicle #${vehicleId}`}
                        value={bookings}
                        subtitle="bookings"
                      />
                    ))}
                  </div>
                </div>
              )}
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default AnalyticsPage;
