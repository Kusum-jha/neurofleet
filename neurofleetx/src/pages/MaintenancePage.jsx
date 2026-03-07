import React, { useState, useEffect } from 'react';
import Navigation from '../../components/Navigation';
import axios from 'axios';
import '../../styles/dashboard.css';
import '../../styles/pages.css';

const MaintenancePage = () => {
  const [maintenanceRecords, setMaintenanceRecords] = useState([]);
  const [alerts, setAlerts] = useState([]);
  const [statistics, setStatistics] = useState({});
  const [newRecord, setNewRecord] = useState({
    vehicleId: '',
    maintenanceType: '',
    description: '',
    scheduledDate: '',
    cost: ''
  });
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('records');

  useEffect(() => {
    fetchMaintenanceData();
  }, []);

  const fetchMaintenanceData = async () => {
    setLoading(true);
    try {
      const [recordsRes, alertsRes, statsRes] = await Promise.all([
        axios.get('http://localhost:8080/api/maintenance/records'),
        axios.get('http://localhost:8080/api/maintenance/alerts'),
        axios.get('http://localhost:8080/api/maintenance/statistics')
      ]);
      
      setMaintenanceRecords(recordsRes.data.data || []);
      setAlerts(alertsRes.data.data || []);
      setStatistics(statsRes.data.data || {});
    } catch (error) {
      console.error('Error fetching maintenance data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateRecord = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        'http://localhost:8080/api/maintenance/records',
        newRecord
      );
      setMaintenanceRecords([response.data.data, ...maintenanceRecords]);
      setNewRecord({
        vehicleId: '',
        maintenanceType: '',
        description: '',
        scheduledDate: '',
        cost: ''
      });
      alert('Maintenance record created successfully');
    } catch (error) {
      console.error('Error creating record:', error);
      alert('Failed to create maintenance record');
    }
  };

  const handleCompleteRecord = async (id) => {
    try {
      const response = await axios.put(
        `http://localhost:8080/api/maintenance/records/${id}/complete`
      );
      setMaintenanceRecords(maintenanceRecords.map(r => 
        r.id === id ? response.data.data : r
      ));
      alert('Maintenance record completed');
    } catch (error) {
      console.error('Error completing record:', error);
      alert('Failed to complete record');
    }
  };

  const handleResolveAlert = async (alertId) => {
    try {
      await axios.put(
        `http://localhost:8080/api/maintenance/alerts/${alertId}/resolve`
      );
      setAlerts(alerts.map(a => 
        a.id === alertId ? { ...a, resolved: true } : a
      ));
      alert('Alert resolved');
    } catch (error) {
      console.error('Error resolving alert:', error);
      alert('Failed to resolve alert');
    }
  };

  const getStatusBadgeColor = (status) => {
    switch (status) {
      case 'SCHEDULED':
        return '#4da6ff';
      case 'IN_PROGRESS':
        return '#ff9500';
      case 'COMPLETED':
        return '#00d084';
      case 'CANCELLED':
        return '#ff6b6b';
      default:
        return '#4da6ff';
    }
  };

  const getSeverityColor = (severity) => {
    switch (severity) {
      case 'LOW':
        return '#00d084';
      case 'MEDIUM':
        return '#ff9500';
      case 'HIGH':
        return '#ff6b6b';
      case 'CRITICAL':
        return '#cc0000';
      default:
        return '#4da6ff';
    }
  };

  return (
    <div className="dashboard-container">
      <Navigation />
      
      <div className="dashboard-header">
        <h1>🔧 Predictive Maintenance Module</h1>
        <p>Monitor and manage vehicle maintenance schedules and alerts</p>
      </div>

      {/* Statistics Cards */}
      <div className="metrics-grid">
        <div className="metric-card glass-card">
          <div className="metric-value">{statistics.totalScheduled || 0}</div>
          <div className="metric-label">Scheduled</div>
        </div>
        <div className="metric-card glass-card">
          <div className="metric-value">{statistics.totalInProgress || 0}</div>
          <div className="metric-label">In Progress</div>
        </div>
        <div className="metric-card glass-card">
          <div className="metric-value">{statistics.totalCompleted || 0}</div>
          <div className="metric-label">Completed</div>
        </div>
        <div className="metric-card glass-card">
          <div className="metric-value">{statistics.unresolvedAlerts || 0}</div>
          <div className="metric-label">Unresolved Alerts</div>
        </div>
        <div className="metric-card glass-card">
          <div className="metric-value">₹{(statistics.totalMaintenanceCost || 0).toFixed(0)}</div>
          <div className="metric-label">Total Cost</div>
        </div>
      </div>

      {/* Tab Navigation */}
      <div className="tab-buttons" style={{ marginTop: '2rem' }}>
        <button
          className={`bubble-btn ${activeTab === 'records' ? 'active' : ''}`}
          onClick={() => setActiveTab('records')}
        >
          📋 Maintenance Records
        </button>
        <button
          className={`bubble-btn ${activeTab === 'alerts' ? 'active' : ''}`}
          onClick={() => setActiveTab('alerts')}
        >
          ⚠️ Alerts ({alerts.filter(a => !a.resolved).length})
        </button>
        <button
          className={`bubble-btn ${activeTab === 'create' ? 'active' : ''}`}
          onClick={() => setActiveTab('create')}
        >
          ➕ Create Record
        </button>
      </div>

      {/* Maintenance Records Tab */}
      {activeTab === 'records' && (
        <div className="content-section glass-card" style={{ marginTop: '2rem', padding: '2rem' }}>
          <h2>Maintenance Records</h2>
          {loading ? (
            <p>Loading...</p>
          ) : maintenanceRecords.length > 0 ? (
            <div style={{ overflowX: 'auto' }}>
              <table className="data-table" style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                  <tr style={{ borderBottom: '2px solid rgba(0, 102, 204, 0.3)' }}>
                    <th style={{ padding: '1rem', textAlign: 'left' }}>Vehicle</th>
                    <th style={{ padding: '1rem', textAlign: 'left' }}>Type</th>
                    <th style={{ padding: '1rem', textAlign: 'left' }}>Status</th>
                    <th style={{ padding: '1rem', textAlign: 'left' }}>Scheduled</th>
                    <th style={{ padding: '1rem', textAlign: 'left' }}>Cost</th>
                    <th style={{ padding: '1rem', textAlign: 'left' }}>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {maintenanceRecords.map((record) => (
                    <tr key={record.id} style={{ borderBottom: '1px solid rgba(0, 102, 204, 0.1)' }}>
                      <td style={{ padding: '1rem' }}>{record.vehicleNumber}</td>
                      <td style={{ padding: '1rem' }}>{record.maintenanceType}</td>
                      <td style={{ padding: '1rem' }}>
                        <span style={{
                          backgroundColor: getStatusBadgeColor(record.status),
                          color: 'white',
                          padding: '0.25rem 0.75rem',
                          borderRadius: '12px',
                          fontSize: '0.875rem',
                          fontWeight: '500'
                        }}>
                          {record.status}
                        </span>
                      </td>
                      <td style={{ padding: '1rem' }}>{record.scheduledDate}</td>
                      <td style={{ padding: '1rem' }}>₹{record.cost || 0}</td>
                      <td style={{ padding: '1rem' }}>
                        {record.status !== 'COMPLETED' && (
                          <button
                            className="bubble-btn-secondary"
                            onClick={() => handleCompleteRecord(record.id)}
                            style={{ padding: '0.5rem 1rem', fontSize: '0.875rem' }}
                          >
                            Complete
                          </button>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <p style={{ color: '#4da6ff', marginTop: '1rem' }}>No maintenance records found</p>
          )}
        </div>
      )}

      {/* Alerts Tab */}
      {activeTab === 'alerts' && (
        <div className="content-section glass-card" style={{ marginTop: '2rem', padding: '2rem' }}>
          <h2>Maintenance Alerts</h2>
          {alerts.filter(a => !a.resolved).length > 0 ? (
            <div style={{ display: 'grid', gap: '1rem' }}>
              {alerts.filter(a => !a.resolved).map((alert) => (
                <div
                  key={alert.id}
                  className="glass-card"
                  style={{
                    padding: '1.5rem',
                    borderLeft: `4px solid ${getSeverityColor(alert.severity)}`,
                    marginBottom: '1rem'
                  }}
                >
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
                    <div style={{ flex: 1 }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.5rem' }}>
                        <strong>{alert.vehicleNumber}</strong>
                        <span style={{
                          backgroundColor: getSeverityColor(alert.severity),
                          color: 'white',
                          padding: '0.25rem 0.75rem',
                          borderRadius: '12px',
                          fontSize: '0.75rem',
                          fontWeight: 'bold'
                        }}>
                          {alert.severity}
                        </span>
                      </div>
                      <p style={{ margin: '0.5rem 0', color: '#4da6ff' }}>{alert.message}</p>
                      <small style={{ color: '#9099a8' }}>Type: {alert.alertType}</small>
                    </div>
                    <button
                      className="bubble-btn"
                      onClick={() => handleResolveAlert(alert.id)}
                      style={{ padding: '0.5rem 1rem', fontSize: '0.875rem' }}
                    >
                      Resolve
                    </button>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p style={{ color: '#00d084', marginTop: '1rem' }}>✅ No unresolved alerts</p>
          )}
        </div>
      )}

      {/* Create Record Tab */}
      {activeTab === 'create' && (
        <div className="content-section glass-card" style={{ marginTop: '2rem', padding: '2rem', maxWidth: '600px' }}>
          <h2>Schedule New Maintenance</h2>
          <form onSubmit={handleCreateRecord} style={{ marginTop: '1.5rem' }}>
            <div className="form-group" style={{ marginBottom: '1.5rem' }}>
              <label className="bubble-label">Vehicle ID</label>
              <input
                type="number"
                className="bubble-input"
                value={newRecord.vehicleId}
                onChange={(e) => setNewRecord({ ...newRecord, vehicleId: e.target.value })}
                required
              />
            </div>

            <div className="form-group" style={{ marginBottom: '1.5rem' }}>
              <label className="bubble-label">Maintenance Type</label>
              <select
                className="bubble-input"
                value={newRecord.maintenanceType}
                onChange={(e) => setNewRecord({ ...newRecord, maintenanceType: e.target.value })}
                required
              >
                <option value="">Select type...</option>
                <option value="Oil Change">Oil Change</option>
                <option value="Tire Rotation">Tire Rotation</option>
                <option value="Battery Check">Battery Check</option>
                <option value="Engine Inspection">Engine Inspection</option>
                <option value="Brake Service">Brake Service</option>
                <option value="Filter Replacement">Filter Replacement</option>
              </select>
            </div>

            <div className="form-group" style={{ marginBottom: '1.5rem' }}>
              <label className="bubble-label">Description</label>
              <textarea
                className="bubble-input"
                value={newRecord.description}
                onChange={(e) => setNewRecord({ ...newRecord, description: e.target.value })}
                rows="3"
                style={{ fontFamily: 'inherit' }}
              ></textarea>
            </div>

            <div className="form-group" style={{ marginBottom: '1.5rem' }}>
              <label className="bubble-label">Scheduled Date</label>
              <input
                type="date"
                className="bubble-input"
                value={newRecord.scheduledDate}
                onChange={(e) => setNewRecord({ ...newRecord, scheduledDate: e.target.value })}
                required
              />
            </div>

            <div className="form-group" style={{ marginBottom: '1.5rem' }}>
              <label className="bubble-label">Estimated Cost</label>
              <input
                type="number"
                className="bubble-input"
                value={newRecord.cost}
                onChange={(e) => setNewRecord({ ...newRecord, cost: e.target.value })}
                step="0.01"
              />
            </div>

            <button type="submit" className="bubble-btn" style={{ width: '100%' }}>
              Schedule Maintenance
            </button>
          </form>
        </div>
      )}
    </div>
  );
};

export default MaintenancePage;
