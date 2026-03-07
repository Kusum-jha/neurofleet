import api from './api';
import { mockAuthService } from './mockApi';

export const authService = {
  register: async (username, email, password, role = 'CUSTOMER', firstName = '', lastName = '') => {
    try {
      const response = await api.post('/auth/register', {
        username,
        email,
        password,
        role,
        firstName,
        lastName,
      });
      return response.data;
    } catch (error) {
      return { success: false, message: error.response?.data?.message || 'Registration failed' };
    }
  },

  login: async (email, password, role = 'CUSTOMER') => {
    try {
      const response = await api.post('/auth/login', {
        email,
        password,
        role,
      });

      if (response.data.data?.token) {
        localStorage.setItem('neurofleetx_token', response.data.data.token);
        localStorage.setItem('neurofleetx_user', JSON.stringify({
          id: response.data.data.userId,
          username: response.data.data.username,
          email: response.data.data.email,
          role: response.data.data.role,
        }));
      }

      return response.data;
    } catch (error) {
      console.log('Backend unavailable, using mock data for demo');
      // Fallback to mock
      return await mockAuthService.login(email, password, role);
    }
  },

  logout: () => {
    localStorage.removeItem('neurofleetx_token');
    localStorage.removeItem('neurofleetx_user');
    localStorage.removeItem('neurofleetx_auth');
  },

  getCurrentUser: () => {
    const user = localStorage.getItem('neurofleetx_user');
    return user ? JSON.parse(user) : null;
  },

  isAuthenticated: () => {
    return !!localStorage.getItem('neurofleetx_token');
  },
};
