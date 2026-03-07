import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { authService } from '../services/authService';
import '../styles/pages.css';

const Login = () => {
  const [email, setEmail] = useState('admin@neurofleetx.com');
  const [password, setPassword] = useState('Admin@123');
  const [role, setRole] = useState('ADMIN');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [emailError, setEmailError] = useState('');
  const navigate = useNavigate();

  const handleEmailChange = (e) => {
    const value = e.target.value;
    setEmail(value);
    setEmailError('');
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
    setError('');
  };

  const handleRoleChange = (e) => {
    setRole(e.target.value);
  };

  const validateEmail = (email) => {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setEmailError('');

    // Validate email
    if (!validateEmail(email)) {
      setEmailError('Please enter a valid email');
      return;
    }

    if (!password) {
      setError('Password is required');
      return;
    }

    setLoading(true);

    // Call backend API
    const result = await authService.login(email, password, role);

    if (result.success && result.data?.token) {
      // Route based on role
      const roleRoutes = {
        'ADMIN': '/admin',
        'FLEET_MANAGER': '/manager',
        'DRIVER': '/driver',
        'CUSTOMER': '/customer',
      };
      navigate(roleRoutes[role] || '/home');
    } else {
      setError(result.message || 'Login failed');
    }

    setLoading(false);
  };

  return (
    <div className="auth-container">
      <div className="auth-card glass-container">
        <div className="auth-header">
          <div className="auth-logo">🔐</div>
          <h1 className="auth-title">Welcome Back</h1>
          <p className="auth-subtitle">Login to your NeuroFleetX account</p>
        </div>

        {error && (
          <div className="message-box message-error">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label className="bubble-label">Select Role</label>
            <select
              className="bubble-input"
              value={role}
              onChange={handleRoleChange}
              disabled={loading}
              style={{ cursor: 'pointer' }}
            >
              <option value="ADMIN">Admin</option>
              <option value="FLEET_MANAGER">Fleet Manager</option>
              <option value="DRIVER">Driver</option>
              <option value="CUSTOMER">Customer</option>
            </select>
          </div>

          <div className="form-group">
            <label className="bubble-label">Email Address</label>
            <input
              type="email"
              className={`bubble-input ${emailError ? 'input-error' : ''}`}
              placeholder="you@example.com"
              value={email}
              onChange={handleEmailChange}
              disabled={loading}
            />
            {emailError && <div className="error-message">{emailError}</div>}
          </div>

          <div className="form-group">
            <label className="bubble-label">Password</label>
            <input
              type="password"
              className="bubble-input"
              placeholder="Enter your password"
              value={password}
              onChange={handlePasswordChange}
              disabled={loading}
            />
          </div>

          <button
            type="submit"
            className="bubble-btn auth-button"
            disabled={loading}
          >
            {loading ? (
              <div className="bubble-loader">
                <span></span>
                <span></span>
                <span></span>
              </div>
            ) : (
              'Sign In'
            )}
          </button>
        </form>

        <div className="auth-footer">
          Don't have an account? <Link to="/register">Sign up here</Link>
        </div>

        <div className="success-banner" style={{ marginTop: '20px', fontSize: '12px' }}>
          📧 Demo Emails:<br/>
          Admin: admin@neurofleetx.com<br/>
          Manager: manager@neurofleetx.com<br/>
          Driver: driver@neurofleetx.com<br/>
          Customer: customer@neurofleetx.com<br/>
          🔑 Backend password: (check database)
        </div>
      </div>
    </div>
  );
};

export default Login;
