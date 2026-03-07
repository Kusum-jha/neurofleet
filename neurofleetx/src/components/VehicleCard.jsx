import '../styles/vehicles.css';

const VehicleCard = ({ vehicle, onEdit, onDelete, onSimulate }) => {
  const getStatusBadgeClass = (status) => {
    switch (status) {
      case 'AVAILABLE':
        return 'available';
      case 'IN_USE':
        return 'in-use';
      case 'MAINTENANCE':
        return 'maintenance';
      default:
        return 'available';
    }
  };

  const getCapacityIcon = (capacity) => {
    if (!capacity) return '🚗';
    if (capacity <= 3) return '🚙';
    if (capacity <= 5) return '🚗';
    if (capacity <= 7) return '🚐';
    return '🚌';
  };

  return (
    <div className="vehicle-card">
      <div className="vehicle-header">
        <div className="vehicle-icon">{getCapacityIcon(vehicle.capacity)}</div>
        <span className={`vehicle-badge ${getStatusBadgeClass(vehicle.status)}`}>
          {vehicle.status}
        </span>
      </div>

      <h3 className="vehicle-title">{vehicle.vehicleType}</h3>
      <p className="vehicle-number">🏷️ {vehicle.vehicleNumber}</p>

      <div className="vehicle-details">
        <div className="detail-item">
          <span className="detail-label">Capacity</span>
          <span className="detail-value">{vehicle.capacity} seats</span>
        </div>
        <div className="detail-item">
          <span className="detail-label">Fuel Type</span>
          <span className="detail-value">{vehicle.fuelType}</span>
        </div>
        <div className="detail-item">
          <span className="detail-label">Mileage</span>
          <span className="detail-value">{vehicle.mileageKm || 0} km</span>
        </div>
        <div className="detail-item">
          <span className="detail-label">Speed</span>
          <span className="detail-value">{vehicle.speedKmh || 0} km/h</span>
        </div>
      </div>

      {vehicle.batteryPercentage !== null && (
        <div className="telemetry-bar">
          <span className="telemetry-label">🔋 Battery</span>
          <div className="bar-container">
            <div className="bar-fill" style={{ width: `${vehicle.batteryPercentage}%` }}></div>
          </div>
          <span className="bar-percentage">{vehicle.batteryPercentage}%</span>
        </div>
      )}

      {vehicle.fuelPercentage !== null && (
        <div className="telemetry-bar">
          <span className="telemetry-label">⛽ Fuel</span>
          <div className="bar-container">
            <div className="bar-fill" style={{ width: `${vehicle.fuelPercentage}%` }}></div>
          </div>
          <span className="bar-percentage">{vehicle.fuelPercentage}%</span>
        </div>
      )}

      {vehicle.currentLatitude && vehicle.currentLongitude && (
        <div className="vehicle-location">
          <span className="vehicle-location-icon">📍</span>
          <span>
            {vehicle.currentLatitude.toFixed(4)}, {vehicle.currentLongitude.toFixed(4)}
          </span>
        </div>
      )}

      <div className="vehicle-actions">
        {onSimulate && (
          <button className="action-btn-small primary" onClick={() => onSimulate(vehicle.id)}>
            📡 Simulate
          </button>
        )}
        {onEdit && (
          <button className="action-btn-small secondary" onClick={() => onEdit(vehicle.id)}>
            ✏️ Edit
          </button>
        )}
        {onDelete && (
          <button className="action-btn-small secondary" onClick={() => onDelete(vehicle.id)}>
            🗑️ Delete
          </button>
        )}
      </div>
    </div>
  );
};

export default VehicleCard;
