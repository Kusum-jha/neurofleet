import { Navigate } from 'react-router-dom';
import { isAuthenticated, getCurrentUser, loginUser } from '../utils/auth';

const ProtectedRoute = ({ children }) => {
  // For now, allow access without login - just auto-login if needed
  if (!isAuthenticated()) {
    loginUser('admin@neurofleetx.com', 'Admin@123');
  }
  return children;
};

export default ProtectedRoute;
