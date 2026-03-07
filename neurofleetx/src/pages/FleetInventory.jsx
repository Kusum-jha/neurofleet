import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import VehicleCard from '../components/VehicleCard';
import { vehicleService } from '../services/vehicleService';
import '../styles/vehicles.css';

const FleetInventory = () => {
  const [vehicles, setVehicles] = useState([]);
  const [filteredVehicles, setFilteredVehicles] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const navigate = useNavigate();

  useEffect(() => {
    fetchVehicles();
    const interval = setInterval(fetchVehicles, 15000); // Refresh every 15 seconds
    return () => clearInterval(interval);
  }, []);

  const fetchVehicles = async () => {
    setIsLoading(true);
    const data = await vehicleService.getAllVehicles();
    setVehicles(data);
    filterVehicles(data, searchTerm, statusFilter);
    setIsLoading(false);
  };

  const filterVehicles = (vehicleList, search, status) => {
    let filtered = vehicleList;

    if (search) {
      filtered = filtered.filter(
        (v) =>
          v.vehicleNumber.toLowerCase().includes(search.toLowerCase()) ||
          v.vehicleType.toLowerCase().includes(search.toLowerCase())
      );
    }

    if (status !== 'ALL') {
      filtered = filtered.filter((v) => v.status === status);
    }

    setFilteredVehicles(filtered);
  };

  const handleSearch = (term) => {
    setSearchTerm(term);
    filterVehicles(vehicles, term, statusFilter);
  };

  const handleStatusFilter = (status) => {
    setStatusFilter(status);
    filterVehicles(vehicles, searchTerm, status);
  };

  const handleSimulate = async (vehicleId) => {
    await vehicleService.simulateTelemetry(vehicleId);
    fetchVehicles();
  };

  const handleDelete = async (vehicleId) => {
    if (window.confirm('Are you sure you want to delete this vehicle?')) {
      await vehicleService.deleteVehicle(vehicleId);
      fetchVehicles();
    }
  };

  return (
    <div className="fleet-container">
      <div className="fleet-header">
        <h1>🚗 Fleet Inventory</h1>
        <p>Manage your vehicle fleet and monitor telemetry</p>
      </div>

      <div className="fleet-controls">
        <input
          type="text"
          className="search-input"
          placeholder="Search by vehicle number or type..."
          value={searchTerm}
          onChange={(e) => handleSearch(e.target.value)}
        />
        <select
          className="filter-select"
          value={statusFilter}
          onChange={(e) => handleStatusFilter(e.target.value)}
        >
          <option value="ALL">All Status</option>
          <option value="AVAILABLE">Available</option>
          <option value="IN_USE">In Use</option>
          <option value="MAINTENANCE">Maintenance</option>
        </select>
        <button className="action-btn-small primary" onClick={() => navigate('/home')}>
          ← Back
        </button>
      </div>

      {isLoading ? (
        <div style={{ textAlign: 'center', padding: '3rem', fontSize: '1.1rem', color: 'var(--ocean-medium)' }}>
          Loading fleet data...
        </div>
      ) : filteredVehicles.length === 0 ? (
        <div className="empty-state">
          <div className="empty-state-icon">🚗</div>
          <h3>No Vehicles Found</h3>
          <p>Try adjusting your search or filters</p>
        </div>
      ) : (
        <>
          <div style={{ marginBottom: '1rem', color: 'var(--ocean-medium)' }}>
            Showing {filteredVehicles.length} of {vehicles.length} vehicles
          </div>
          <div className="vehicle-grid">
            {filteredVehicles.map((vehicle) => (
              <VehicleCard
                key={vehicle.id}
                vehicle={vehicle}
                onSimulate={handleSimulate}
                onDelete={handleDelete}
              />
            ))}
          </div>
        </>
      )}
    </div>
  );
};

export default FleetInventory;
