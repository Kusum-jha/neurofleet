import React, { useState, useEffect } from 'react';
import Navigation from '../components/Navigation';
import axios from 'axios';
import '../styles/dashboard.css';
import '../styles/pages.css';

const BookingsPage = () => {
  const [bookings, setBookings] = useState([]);
  const [loyaltyPoints, setLoyaltyPoints] = useState(null);
  const [statistics, setStatistics] = useState({});
  const [newBooking, setNewBooking] = useState({
    vehicleId: '',
    customerId: '',
    pickupLatitude: '',
    pickupLongitude: '',
    dropoffLatitude: '',
    dropoffLongitude: '',
    pickupTime: '',
    distanceKm: ''
  });
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('bookings');
  const currentUser = JSON.parse(localStorage.getItem('neurofleetx_auth') || '{}');

  useEffect(() => {
    fetchBookingData();
  }, []);

  const fetchBookingData = async () => {
    setLoading(true);
    try {
      const [bookingsRes, statsRes] = await Promise.all([
        axios.get('http://localhost:8080/api/bookings'),
        axios.get('http://localhost:8080/api/bookings/statistics/summary')
      ]);
      
      setBookings(bookingsRes.data.data || []);
      setStatistics(statsRes.data.data || {});

      if (currentUser.userId) {
        try {
          const loyaltyRes = await axios.get(
            `http://localhost:8080/api/bookings/loyalty/${currentUser.userId}`
          );
          setLoyaltyPoints(loyaltyRes.data.data);
        } catch (error) {
          console.log('Loyalty points not found, initializing...');
          const initRes = await axios.post(
            `http://localhost:8080/api/bookings/loyalty/${currentUser.userId}/initialize`
          );
          setLoyaltyPoints(initRes.data.data);
        }
      }
    } catch (error) {
      console.error('Error fetching booking data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateBooking = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        'http://localhost:8080/api/bookings',
        {
          ...newBooking,
          customerId: currentUser.userId,
          vehicleId: parseInt(newBooking.vehicleId),
          pickupLatitude: parseFloat(newBooking.pickupLatitude),
          pickupLongitude: parseFloat(newBooking.pickupLongitude),
          dropoffLatitude: parseFloat(newBooking.dropoffLatitude),
          dropoffLongitude: parseFloat(newBooking.dropoffLongitude),
          distanceKm: parseFloat(newBooking.distanceKm) || null
        }
      );
      setBookings([response.data.data, ...bookings]);
      setNewBooking({
        vehicleId: '',
        customerId: '',
        pickupLatitude: '',
        pickupLongitude: '',
        dropoffLatitude: '',
        dropoffLongitude: '',
        pickupTime: '',
        distanceKm: ''
      });
      alert('Booking created successfully!');
      fetchBookingData();
    } catch (error) {
      console.error('Error creating booking:', error);
      alert('Failed to create booking');
    }
  };

  const handleCompleteBooking = async (id) => {
    const actualFare = prompt('Enter actual fare:');
    if (actualFare) {
      try {
        const response = await axios.put(
          `http://localhost:8080/api/bookings/${id}/complete`,
          { actualFare: parseFloat(actualFare) }
        );
        setBookings(bookings.map(b => b.id === id ? response.data.data : b));
        alert('Booking completed');
        fetchBookingData();
      } catch (error) {
        console.error('Error completing booking:', error);
        alert('Failed to complete booking');
      }
    }
  };

  const handleCancelBooking = async (id) => {
    if (window.confirm('Are you sure you want to cancel this booking?')) {
      try {
        const response = await axios.put(
          `http://localhost:8080/api/bookings/${id}/cancel`
        );
        setBookings(bookings.map(b => b.id === id ? response.data.data : b));
        alert('Booking cancelled');
      } catch (error) {
        console.error('Error cancelling booking:', error);
        alert('Failed to cancel booking');
      }
    }
  };

  const handleRedeemPoints = async (points) => {
    if (points > loyaltyPoints.pointsBalance) {
      alert('Insufficient loyalty points');
      return;
    }
    try {
      const response = await axios.put(
        `http://localhost:8080/api/bookings/loyalty/${currentUser.userId}/redeem/${points}`
      );
      setLoyaltyPoints(response.data.data);
      alert('Points redeemed successfully!');
    } catch (error) {
      console.error('Error redeeming points:', error);
      alert('Failed to redeem points');
    }
  };

  const getStatusBadgeColor = (status) => {
    switch (status) {
      case 'PENDING':
        return '#ff9500';
      case 'CONFIRMED':
        return '#4da6ff';
      case 'IN_PROGRESS':
        return '#00b4ff';
      case 'COMPLETED':
        return '#00d084';
      case 'CANCELLED':
        return '#ff6b6b';
      default:
        return '#4da6ff';
    }
  };

  return (
    <div className="dashboard-container">
      <Navigation />
      
      <div className="dashboard-header">
        <h1>🚗 Customer Booking & Loyalty Module</h1>
        <p>Book vehicles and track your loyalty rewards</p>
      </div>

      {/* Statistics Cards */}
      <div className="metrics-grid">
        <div className="metric-card glass-card">
          <div className="metric-value">{statistics.totalBookings || 0}</div>
          <div className="metric-label">Total Bookings</div>
        </div>
        <div className="metric-card glass-card">
          <div className="metric-value">{statistics.completedBookings || 0}</div>
          <div className="metric-label">Completed</div>
        </div>
        <div className="metric-card glass-card">
          <div className="metric-value">₹{(statistics.totalRevenue || 0).toFixed(0)}</div>
          <div className="metric-label">Total Revenue</div>
        </div>
        <div className="metric-card glass-card">
          <div className="metric-value">{(statistics.totalDistance || 0).toFixed(0)} km</div>
          <div className="metric-label">Total Distance</div>
        </div>
      </div>

      {/* Loyalty Points Card */}
      {loyaltyPoints && (
        <div className="glass-card" style={{ marginTop: '2rem', padding: '2rem', maxWidth: '100%' }}>
          <h2 style={{ marginBottom: '1rem' }}>⭐ Your Loyalty Points</h2>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
            <div className="metric-card glass-card">
              <div className="metric-value">{loyaltyPoints.pointsBalance}</div>
              <div className="metric-label">Current Balance</div>
            </div>
            <div className="metric-card glass-card">
              <div className="metric-value">{loyaltyPoints.totalPointsEarned}</div>
              <div className="metric-label">Total Earned</div>
            </div>
            <div className="metric-card glass-card">
              <div className="metric-value">{loyaltyPoints.totalPointsRedeemed}</div>
              <div className="metric-label">Total Redeemed</div>
            </div>
          </div>
          <div style={{ marginTop: '1.5rem', display: 'flex', gap: '1rem' }}>
            <button
              className="bubble-btn"
              onClick={() => handleRedeemPoints(100)}
              disabled={loyaltyPoints.pointsBalance < 100}
            >
              Redeem 100 Points
            </button>
            <button
              className="bubble-btn"
              onClick={() => handleRedeemPoints(500)}
              disabled={loyaltyPoints.pointsBalance < 500}
            >
              Redeem 500 Points
            </button>
          </div>
        </div>
      )}

      {/* Tab Navigation */}
      <div className="tab-buttons" style={{ marginTop: '2rem' }}>
        <button
          className={`bubble-btn ${activeTab === 'bookings' ? 'active' : ''}`}
          onClick={() => setActiveTab('bookings')}
        >
          📋 My Bookings
        </button>
        <button
          className={`bubble-btn ${activeTab === 'create' ? 'active' : ''}`}
          onClick={() => setActiveTab('create')}
        >
          ➕ New Booking
        </button>
      </div>

      {/* Bookings Tab */}
      {activeTab === 'bookings' && (
        <div className="content-section glass-card" style={{ marginTop: '2rem', padding: '2rem' }}>
          <h2>Your Bookings</h2>
          {loading ? (
            <p>Loading...</p>
          ) : bookings.length > 0 ? (
            <div style={{ display: 'grid', gap: '1.5rem' }}>
              {bookings.map((booking) => (
                <div key={booking.id} className="glass-card" style={{ padding: '1.5rem' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', marginBottom: '1rem' }}>
                    <div>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '0.5rem' }}>
                        <strong style={{ fontSize: '1.1rem' }}>{booking.bookingNumber}</strong>
                        <span style={{
                          backgroundColor: getStatusBadgeColor(booking.status),
                          color: 'white',
                          padding: '0.25rem 0.75rem',
                          borderRadius: '12px',
                          fontSize: '0.875rem',
                          fontWeight: '500'
                        }}>
                          {booking.status}
                        </span>
                      </div>
                      <p style={{ margin: '0.25rem 0', color: '#4da6ff' }}>Vehicle: {booking.vehicleNumber}</p>
                    </div>
                  </div>

                  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', gap: '1rem', marginBottom: '1rem' }}>
                    <div>
                      <small style={{ color: '#9099a8' }}>Pickup Time</small>
                      <p style={{ margin: '0.25rem 0' }}>{booking.pickupTime ? new Date(booking.pickupTime).toLocaleString() : 'N/A'}</p>
                    </div>
                    <div>
                      <small style={{ color: '#9099a8' }}>Distance</small>
                      <p style={{ margin: '0.25rem 0' }}>{booking.distanceKm} km</p>
                    </div>
                    <div>
                      <small style={{ color: '#9099a8' }}>Estimated Fare</small>
                      <p style={{ margin: '0.25rem 0' }}>₹{booking.estimatedFare || 0}</p>
                    </div>
                    <div>
                      <small style={{ color: '#9099a8' }}>Actual Fare</small>
                      <p style={{ margin: '0.25rem 0' }}>₹{booking.actualFare || 'Pending'}</p>
                    </div>
                  </div>

                  <div style={{ display: 'flex', gap: '1rem' }}>
                    {booking.status === 'PENDING' && (
                      <button
                        className="bubble-btn-secondary"
                        onClick={() => handleCancelBooking(booking.id)}
                      >
                        Cancel Booking
                      </button>
                    )}
                    {booking.status === 'IN_PROGRESS' && (
                      <button
                        className="bubble-btn"
                        onClick={() => handleCompleteBooking(booking.id)}
                      >
                        Complete Booking
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p style={{ color: '#4da6ff', marginTop: '1rem' }}>No bookings yet. Create your first booking!</p>
          )}
        </div>
      )}

      {/* Create Booking Tab */}
      {activeTab === 'create' && (
        <div className="content-section glass-card" style={{ marginTop: '2rem', padding: '2rem', maxWidth: '600px' }}>
          <h2>Book a Vehicle</h2>
          <form onSubmit={handleCreateBooking} style={{ marginTop: '1.5rem' }}>
            <div className="form-group" style={{ marginBottom: '1.5rem' }}>
              <label className="bubble-label">Vehicle ID</label>
              <input
                type="number"
                className="bubble-input"
                value={newBooking.vehicleId}
                onChange={(e) => setNewBooking({ ...newBooking, vehicleId: e.target.value })}
                placeholder="Enter vehicle ID"
                required
              />
            </div>

            <div className="form-group" style={{ marginBottom: '1.5rem' }}>
              <label className="bubble-label">Pickup Latitude</label>
              <input
                type="number"
                step="any"
                className="bubble-input"
                value={newBooking.pickupLatitude}
                onChange={(e) => setNewBooking({ ...newBooking, pickupLatitude: e.target.value })}
                placeholder="e.g., 28.7041"
                required
              />
            </div>

            <div className="form-group" style={{ marginBottom: '1.5rem' }}>
              <label className="bubble-label">Pickup Longitude</label>
              <input
                type="number"
                step="any"
                className="bubble-input"
                value={newBooking.pickupLongitude}
                onChange={(e) => setNewBooking({ ...newBooking, pickupLongitude: e.target.value })}
                placeholder="e.g., 77.1025"
                required
              />
            </div>

            <div className="form-group" style={{ marginBottom: '1.5rem' }}>
              <label className="bubble-label">Dropoff Latitude</label>
              <input
                type="number"
                step="any"
                className="bubble-input"
                value={newBooking.dropoffLatitude}
                onChange={(e) => setNewBooking({ ...newBooking, dropoffLatitude: e.target.value })}
                placeholder="e.g., 28.6139"
                required
              />
            </div>

            <div className="form-group" style={{ marginBottom: '1.5rem' }}>
              <label className="bubble-label">Dropoff Longitude</label>
              <input
                type="number"
                step="any"
                className="bubble-input"
                value={newBooking.dropoffLongitude}
                onChange={(e) => setNewBooking({ ...newBooking, dropoffLongitude: e.target.value })}
                placeholder="e.g., 77.2090"
                required
              />
            </div>

            <div className="form-group" style={{ marginBottom: '1.5rem' }}>
              <label className="bubble-label">Pickup Time</label>
              <input
                type="datetime-local"
                className="bubble-input"
                value={newBooking.pickupTime}
                onChange={(e) => setNewBooking({ ...newBooking, pickupTime: e.target.value })}
                required
              />
            </div>

            <div className="form-group" style={{ marginBottom: '1.5rem' }}>
              <label className="bubble-label">Distance (km)</label>
              <input
                type="number"
                step="0.1"
                className="bubble-input"
                value={newBooking.distanceKm}
                onChange={(e) => setNewBooking({ ...newBooking, distanceKm: e.target.value })}
                placeholder="Optional"
              />
            </div>

            <button type="submit" className="bubble-btn" style={{ width: '100%' }}>
              Create Booking
            </button>
          </form>
        </div>
      )}
    </div>
  );
};

export default BookingsPage;
