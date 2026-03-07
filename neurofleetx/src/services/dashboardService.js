import api from './api';
import { mockDashboardService } from './mockApi';

export const dashboardService = {
  getAdminMetrics: async () => {
    try {
      const response = await api.get('/dashboard/metrics/admin');
      return response.data.data;
    } catch (error) {
      console.log('Using mock admin metrics');
      return await mockDashboardService.getAdminMetrics();
    }
  },

  getManagerMetrics: async () => {
    try {
      const response = await api.get('/dashboard/metrics/manager');
      return response.data.data;
    } catch (error) {
      console.log('Using mock manager metrics');
      return await mockDashboardService.getManagerMetrics();
    }
  },

  getDriverMetrics: async () => {
    try {
      const response = await api.get('/dashboard/metrics/driver');
      return response.data.data;
    } catch (error) {
      console.log('Using mock driver metrics');
      return await mockDashboardService.getDriverMetrics();
    }
  },

  getCustomerMetrics: async () => {
    try {
      const response = await api.get('/dashboard/metrics/customer');
      return response.data.data;
    } catch (error) {
      console.log('Using mock customer metrics');
      return await mockDashboardService.getCustomerMetrics();
    }
  },

  getMetricsByRole: async (role) => {
    try {
      const response = await api.get(`/dashboard/${role}`);
      return response.data.data;
    } catch (error) {
      console.error(`Error fetching ${role} metrics:`, error);
      return {};
    }
  },
};
