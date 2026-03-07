import api from './api';
import { mockRouteService } from './mockApi';

export const routeService = {
  getAllRoutes: async () => {
    try {
      const response = await api.get('/routes');
      return response.data.data || [];
    } catch (error) {
      console.error('Error fetching routes:', error);
      return [];
    }
  },

  getRouteById: async (id) => {
    try {
      const response = await api.get(`/routes/${id}`);
      return response.data.data;
    } catch (error) {
      console.error('Error fetching route:', error);
      return null;
    }
  },

  getRoutesByVehicle: async (vehicleId) => {
    try {
      const response = await api.get(`/routes/vehicle/${vehicleId}`);
      return response.data.data || [];
    } catch (error) {
      console.error('Error fetching routes:', error);
      return [];
    }
  },

  getRoutesByStatus: async (status) => {
    try {
      const response = await api.get(`/routes/status/${status}`);
      return response.data.data || [];
    } catch (error) {
      console.error('Error fetching routes:', error);
      return [];
    }
  },

  createRoute: async (routeData) => {
    try {
      const response = await api.post('/routes', routeData);
      return response.data;
    } catch (error) {
      return { success: false, message: error.response?.data?.message || 'Failed to create route' };
    }
  },

  updateRouteStatus: async (id, status) => {
    try {
      const response = await api.put(`/routes/${id}/status?status=${status}`);
      return response.data;
    } catch (error) {
      return { success: false, message: error.response?.data?.message || 'Failed to update route' };
    }
  },

  deleteRoute: async (id) => {
    try {
      const response = await api.delete(`/routes/${id}`);
      return response.data;
    } catch (error) {
      return { success: false, message: error.response?.data?.message || 'Failed to delete route' };
    }
  },

  optimizeRoute: async (vehicleId, startLat, startLng, endLat, endLng) => {
    try {
      const response = await api.post(
        `/routes/optimize?vehicleId=${vehicleId}&startLat=${startLat}&startLng=${startLng}&endLat=${endLat}&endLng=${endLng}`
      );
      return response.data.data || [];
    } catch (error) {
      console.log('Using mock route optimization');
      return await mockRouteService.optimizeRoute(vehicleId, startLat, startLng, endLat, endLng);
    }
  },
};
