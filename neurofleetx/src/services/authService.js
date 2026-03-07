import api from './api';
import { mockAuthService } from './mockApi';
import { loginUser as localStorageLogin } from '../utils/auth';

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

      // Check if backend returned success
      if (response.data?.success === false) {
        console.log('Backend authentication failed, trying local authentication');
        throw new Error('Backend auth failed');
      }

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
      console.log('Backend authentication failed, trying mock and local authentication');
      
      // First try mock service
      const mockResult = await mockAuthService.login(email, password, role);
      if (mockResult.success) {
        console.log('Mock authentication succeeded');
        return mockResult;
      }
      
      // If mock fails, try localStorage users
      const localResult = localStorageLogin(email, password);
      if (localResult.success) {
        const token = 'local-token-' + Date.now();
        localStorage.setItem('neurofleetx_token', token);
        localStorage.setItem('neurofleetx_user', JSON.stringify({
          id: localResult.user.id,
          username: localResult.user.username,
          email: localResult.user.email,
          role: localResult.user.role || role,
        }));
        console.log('Local authentication succeeded');
        return {
          success: true,
          data: {
            token: token,
            userId: localResult.user.id,
            username: localResult.user.username,
            email: email,
            role: localResult.user.role || role,
          },
          message: 'Login successful',
        };
      }
      console.log('All authentication methods failed');
      return localResult;
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
