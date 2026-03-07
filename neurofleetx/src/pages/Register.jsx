import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { registerUser, validateEmail, validatePassword, validateUsername } from '../utils/auth';
import '../styles/pages.css';

const Register = () => {
  const [formData, setFormData] = useState({
    email: '',
    username: '',
    password: '',
    confirmPassword: ''
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    // Clear error for this field
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.email) {
      newErrors.email = 'Email is required';
    } else if (!validateEmail(formData.email)) {
      newErrors.email = 'Please enter a valid email';
    }

    if (!formData.username) {
      newErrors.username = 'Username is required';
    } else if (!validateUsername(formData.username)) {
      newErrors.username = 'Username must be 3-20 characters (letters, numbers, - or _)';
    }

    if (!formData.password) {
      newErrors.password = 'Password is required';
    } else {
      const passwordCheck = validatePassword(formData.password);
      if (!passwordCheck.isStrong) {
        newErrors.password = 'Password must be at least 8 characters';
      } else if (!passwordCheck.hasUpperCase) {
        newErrors.password = 'Password must contain at least one uppercase letter';
      } else if (!passwordCheck.hasLowerCase) {
        newErrors.password = 'Password must contain at least one lowercase letter';
      } else if (!passwordCheck.hasNumbers) {
        newErrors.password = 'Password must contain at least one number';
      }
    }

    if (!formData.confirmPassword) {
      newErrors.confirmPassword = 'Please confirm your password';
    } else if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = 'Passwords do not match';
    }

    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});
    setSuccessMessage('');

    const newErrors = validateForm();
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    setLoading(true);

    // Simulate API call
    setTimeout(() => {
      const result = registerUser(formData.email, formData.password, formData.username);

      if (result.success) {
        setSuccessMessage('✅ Account created successfully! Redirecting to login...');
        setTimeout(() => {
          navigate('/login');
        }, 2000);
      } else {
        setErrors({ submit: result.error });
      }

      setLoading(false);
    }, 800);
  };

  return (
    <div className="auth-container">
      <div className="auth-card glass-container">
        <div className="auth-header">
          <div className="auth-logo">🚀</div>
          <h1 className="auth-title">Join NeuroFleetX</h1>
          <p className="auth-subtitle">Create your account and get started</p>
        </div>

        {errors.submit && (
          <div className="message-box message-error">
            {errors.submit}
          </div>
        )}

        {successMessage && (
          <div className="message-box message-success">
            {successMessage}
          </div>
        )}

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label className="bubble-label">Email Address</label>
            <input
              type="email"
              name="email"
              className={`bubble-input ${errors.email ? 'input-error' : ''}`}
              placeholder="you@example.com"
              value={formData.email}
              onChange={handleChange}
              disabled={loading}
            />
            {errors.email && <div className="error-message">{errors.email}</div>}
          </div>

          <div className="form-group">
            <label className="bubble-label">Username</label>
            <input
              type="text"
              name="username"
              className={`bubble-input ${errors.username ? 'input-error' : ''}`}
              placeholder="your_username"
              value={formData.username}
              onChange={handleChange}
              disabled={loading}
            />
            {errors.username && <div className="error-message">{errors.username}</div>}
          </div>

          <div className="form-group">
            <label className="bubble-label">Password</label>
            <input
              type="password"
              name="password"
              className={`bubble-input ${errors.password ? 'input-error' : ''}`}
              placeholder="Create a strong password"
              value={formData.password}
              onChange={handleChange}
              disabled={loading}
            />
            {errors.password && <div className="error-message">{errors.password}</div>}
            <small style={{ color: 'rgba(255, 255, 255, 0.6)', marginTop: '4px', display: 'block' }}>
              Min 8 chars, uppercase, lowercase, number
            </small>
          </div>

          <div className="form-group">
            <label className="bubble-label">Confirm Password</label>
            <input
              type="password"
              name="confirmPassword"
              className={`bubble-input ${errors.confirmPassword ? 'input-error' : ''}`}
              placeholder="Confirm your password"
              value={formData.confirmPassword}
              onChange={handleChange}
              disabled={loading}
            />
            {errors.confirmPassword && <div className="error-message">{errors.confirmPassword}</div>}
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
              'Create Account'
            )}
          </button>
        </form>

        <div className="auth-footer">
          Already have an account? <Link to="/login">Sign in here</Link>
        </div>
      </div>
    </div>
  );
};

export default Register;
