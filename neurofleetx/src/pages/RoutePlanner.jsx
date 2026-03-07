import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import MapContainer from '../components/MapContainer';
import { routeService } from '../services/routeService';
import { vehicleService } from '../services/vehicleService';
import '../styles/routes.css';

const RoutePlanner = () => {
  const [vehicles, setVehicles] = useState([]);
  const [selectedVehicle, setSelectedVehicle] = useState(null);
  const [startLat, setStartLat] = useState(28.6139);
  const [startLng, setStartLng] = useState(77.209);
  const [endLat, setEndLat] = useState(28.5355);
  const [endLng, setEndLng] = useState(77.391);
  const [optimizedRoutes, setOptimizedRoutes] = useState([]);
  const [isOptimizing, setIsOptimizing] = useState(false);
  const [routeType, setRouteType] = useState('shortest');
  const navigate = useNavigate();

  useEffect(() => {
    fetchVehicles();
  }, []);

  const fetchVehicles = async () => {
    const data = await vehicleService.getAllVehicles();
    setVehicles(data);
    if (data.length > 0) {
      setSelectedVehicle(data[0].id);
    }
  };

  const handleOptimize = async () => {
    if (!selectedVehicle) {
      alert('Please select a vehicle');
      return;
    }

    setIsOptimizing(true);
    const routes = await routeService.optimizeRoute(
      selectedVehicle,
      parseFloat(startLat),
      parseFloat(startLng),
      parseFloat(endLat),
      parseFloat(endLng)
    );
    setOptimizedRoutes(routes);
    setIsOptimizing(false);
  };

  const handleSelectRoute = async (route) => {
    // In a real app, this would save the route and start navigation
    alert(`Selected route: ${route.distanceKm.toFixed(1)} km in ${route.estimatedDurationMinutes} minutes`);
  };

  const getRouteTypeLabel = (index) => {
    const types = ['Shortest', 'Fastest', 'Most Efficient'];
    const badges = ['shortest', 'fastest', 'efficient'];
    return { label: types[index], badge: badges[index] };
  };

  return (
    <div className="routes-container">
      <div className="routes-header">
        <h1>🗺️ Route Planner & Optimization</h1>
        <p>AI-powered route optimization for your fleet</p>
      </div>

      <div className="routes-layout">
        {/* Planner Panel */}
        <div className="planner-panel">
          <div className="planner-section">
            <h3>🚗 Select Vehicle</h3>
            <select
              className="planner-input"
              value={selectedVehicle || ''}
              onChange={(e) => setSelectedVehicle(parseInt(e.target.value))}
            >
              <option value="">Choose a vehicle...</option>
              {vehicles.map((v) => (
                <option key={v.id} value={v.id}>
                  {v.vehicleNumber} - {v.vehicleType}
                </option>
              ))}
            </select>
          </div>

          <div className="planner-section">
            <h3>📍 Start Location</h3>
            <label className="input-label">Latitude</label>
            <input
              type="number"
              className="planner-input"
              placeholder="Enter latitude"
              value={startLat}
              onChange={(e) => setStartLat(e.target.value)}
              step="0.0001"
            />
            <label className="input-label">Longitude</label>
            <input
              type="number"
              className="planner-input"
              placeholder="Enter longitude"
              value={startLng}
              onChange={(e) => setStartLng(e.target.value)}
              step="0.0001"
            />
          </div>

          <div className="planner-section">
            <h3>🎯 End Location</h3>
            <label className="input-label">Latitude</label>
            <input
              type="number"
              className="planner-input"
              placeholder="Enter latitude"
              value={endLat}
              onChange={(e) => setEndLat(e.target.value)}
              step="0.0001"
            />
            <label className="input-label">Longitude</label>
            <input
              type="number"
              className="planner-input"
              placeholder="Enter longitude"
              value={endLng}
              onChange={(e) => setEndLng(e.target.value)}
              step="0.0001"
            />
          </div>

          <div className="planner-section">
            <h3>⚙️ Route Type</h3>
            <div className="route-options">
              {['Shortest', 'Fastest', 'Efficient'].map((type, idx) => (
                <button
                  key={idx}
                  className={`route-option ${routeType === type.toLowerCase() ? 'active' : ''}`}
                  onClick={() => setRouteType(type.toLowerCase())}
                >
                  {type}
                </button>
              ))}
            </div>
          </div>

          <button className="optimize-btn" onClick={handleOptimize} disabled={isOptimizing}>
            {isOptimizing ? '⏳ Optimizing...' : '⚡ Optimize Route'}
          </button>

          <button
            className="optimize-btn"
            style={{ marginTop: '1rem', background: 'rgba(0, 100, 200, 0.2)', color: '#0066cc' }}
            onClick={() => navigate('/home')}
          >
            ← Back
          </button>
        </div>

        {/* Map */}
        <div className="map-container">
          <MapContainer routes={optimizedRoutes} vehicles={vehicles.slice(0, 3)} />
        </div>
      </div>

      {/* Results */}
      {optimizedRoutes.length > 0 && (
        <div className="routes-results">
          <h2 style={{ marginTop: 0 }}>✨ Optimized Route Options</h2>
          <div>
            {optimizedRoutes.map((route, index) => {
              const { label, badge } = getRouteTypeLabel(index);
              return (
                <div key={index} className="route-card">
                  <div className="route-title">
                    {label} Route
                    <span className={`route-badge ${badge}`}>{badge}</span>
                  </div>

                  <div className="route-stats">
                    <div className="stat-item">
                      <div className="stat-label">Distance</div>
                      <div className="stat-value">{route.distanceKm.toFixed(1)} km</div>
                    </div>
                    <div className="stat-item">
                      <div className="stat-label">Duration</div>
                      <div className="stat-value">{route.estimatedDurationMinutes} min</div>
                    </div>
                    <div className="stat-item">
                      <div className="stat-label">Avg Speed</div>
                      <div className="stat-value">
                        {(route.distanceKm.toFixed(1) / (route.estimatedDurationMinutes / 60)).toFixed(0)} km/h
                      </div>
                    </div>
                  </div>

                  <div className="route-description">
                    📊 This is the {label.toLowerCase()} option for your route. Optimization engine has processed traffic patterns and vehicle data to suggest this path.
                  </div>

                  <div className="route-actions">
                    <button className="route-btn primary" onClick={() => handleSelectRoute(route)}>
                      ✅ Select Route
                    </button>
                    <button className="route-btn secondary">📍 Details</button>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      )}
    </div>
  );
};

export default RoutePlanner;
