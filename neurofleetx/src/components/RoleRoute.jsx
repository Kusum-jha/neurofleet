import { Navigate } from 'react-router-dom';
import { authService } from '../services/authService';

const RoleRoute = ({ children, requiredRole }) => {
  const user = authService.getCurrentUser();

  if (!user) {
    return <Navigate to="/login" />;
  }

  if (requiredRole && user.role !== requiredRole) {
    return <Navigate to="/home" />;
  }

  return children;
};

export default RoleRoute;
