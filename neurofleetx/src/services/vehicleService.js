import api from './api';
import { mockVehicleService } from './mockApi';

export const vehicleService = {
  getAllVehicles: async () => {
    try {
      const response = await api.get('/vehicles');
      return response.data.data || [];
    } catch (error) {
      console.log('Backend unavailable, using mock vehicles');
      return await mockVehicleService.getAllVehicles();
    }
  },

  getVehicleById: async (id) => {
    try {
      const response = await api.get(`/vehicles/${id}`);
      return response.data.data;
    } catch (error) {
      console.error('Error fetching vehicle:', error);
      return null;
    }
  },

  getVehiclesByStatus: async (status) => {
    try {
      const response = await api.get(`/vehicles/status/${status}`);
      return response.data.data || [];
    } catch (error) {
      console.error('Error fetching vehicles by status:', error);
      return [];
    }
  },

  createVehicle: async (vehicleData) => {
    try {
      const response = await api.post('/vehicles', vehicleData);
      return response.data;
    } catch (error) {
      return { success: false, message: error.response?.data?.message || 'Failed to create vehicle' };
    }
  },

  updateVehicle: async (id, vehicleData) => {
    try {
      const response = await api.put(`/vehicles/${id}`, vehicleData);
      return response.data;
    } catch (error) {
      return { success: false, message: error.response?.data?.message || 'Failed to update vehicle' };
    }
  },

  deleteVehicle: async (id) => {
    try {
      const response = await api.delete(`/vehicles/${id}`);
      return response.data;
    } catch (error) {
      return { success: false, message: error.response?.data?.message || 'Failed to delete vehicle' };
    }
  },

  getLatestTelemetry: async (vehicleId) => {
    try {
      const response = await api.get(`/telemetry/vehicles/${vehicleId}/latest`);
      return response.data.data;
    } catch (error) {
      console.error('Error fetching telemetry:', error);
      return null;
    }
  },

  getTelemetryHistory: async (vehicleId, hours = 24) => {
    try {
      const response = await api.get(`/telemetry/vehicles/${vehicleId}/history?hours=${hours}`);
      return response.data.data || [];
    } catch (error) {
      console.error('Error fetching telemetry history:', error);
      return [];
    }
  },

  simulateTelemetry: async (vehicleId) => {
    try {
      const response = await api.post(`/telemetry/vehicles/${vehicleId}/simulate`);
      return response.data.data;
    } catch (error) {
      console.log('Using mock telemetry simulation');
      return await mockVehicleService.simulateTelemetry(vehicleId);
    }
  },

  getFleetStatus: async () => {
    try {
      const response = await api.get('/vehicles/fleet/status');
      return response.data.data;
    } catch (error) {
      console.error('Error fetching fleet status:', error);
      return null;
    }
  },
};
