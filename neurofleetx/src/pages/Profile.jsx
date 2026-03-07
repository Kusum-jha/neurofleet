import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCurrentUser, getUserProfile, updateProfileField, logoutUser } from '../utils/auth';
import '../styles/pages.css';

const Profile = () => {
  const navigate = useNavigate();
  const currentUser = getCurrentUser();
  const [userData, setUserData] = useState(null);
  const [editingField, setEditingField] = useState(null);
  const [editValue, setEditValue] = useState('');
  const [saveStatus, setSaveStatus] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!currentUser) {
      navigate('/login');
      return;
    }

    // Load user profile
    const profile = getUserProfile(currentUser.userId);
    setUserData(profile);
    setLoading(false);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleEditField = (fieldName, currentValue) => {
    setEditingField(fieldName);
    setEditValue(currentValue || '');
    setSaveStatus('');
  };

  const handleSaveField = (fieldName) => {
    if (editValue.trim() === '') {
      setSaveStatus('error: Field cannot be empty');
      return;
    }

    // Simulate API call
    setLoading(true);
    setTimeout(() => {
      const result = updateProfileField(currentUser.userId, fieldName, editValue);

      if (result.success) {
        setUserData(prev => ({
          ...prev,
          profile: {
            ...prev.profile,
            [fieldName]: editValue
          }
        }));
        setEditingField(null);
        setSaveStatus('success');
        setTimeout(() => setSaveStatus(''), 2000);
      } else {
        setSaveStatus('error: ' + result.error);
      }
      setLoading(false);
    }, 400);
  };

  const handleCancel = () => {
    setEditingField(null);
    setSaveStatus('');
  };

  const handleLogout = () => {
    logoutUser();
    navigate('/login');
  };

  if (loading) {
    return (
      <div className="profile-container" style={{ paddingTop: '80px' }}>
        <div className="glass-container flex-center" style={{ minHeight: '300px' }}>
          <div className="bubble-loader">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>
    );
  }

  if (!userData) {
    return (
      <div className="profile-container">
        <div className="glass-container message-box message-error">
          Profile not found. Please log in again.
        </div>
      </div>
    );
  }

  const profileFields = [
    { key: 'firstName', label: 'First Name', icon: '👤' },
    { key: 'lastName', label: 'Last Name', icon: '👤' },
    { key: 'bio', label: 'Bio', icon: '📝' },
    { key: 'location', label: 'Location', icon: '📍' },
    { key: 'phone', label: 'Phone', icon: '📱' }
  ];

  return (
    <div className="profile-container">
      <div className="glass-container">
        <div className="profile-header">
          <div className="profile-avatar">💼</div>
          <h1 className="profile-title">{userData.username}</h1>
          <p className="profile-email">{userData.email}</p>
          <small style={{ color: 'rgba(255, 255, 255, 0.5)' }}>
            Member since {new Date(userData.createdAt).toLocaleDateString()}
          </small>
        </div>

        {saveStatus === 'success' && (
          <div className="message-box message-success">
            ✅ Profile updated successfully!
          </div>
        )}

        {saveStatus.startsWith('error') && (
          <div className="message-box message-error">
            {saveStatus.replace('error: ', '')}
          </div>
        )}

        <div className="profile-section">
          <h3 className="profile-section-title">📋 Personal Information</h3>

          {profileFields.map(field => (
            <div key={field.key} className="profile-field">
              <label className="profile-field-label">{field.icon} {field.label}</label>

              {editingField === field.key ? (
                <div>
                  <input
                    type="text"
                    className="bubble-input profile-field-input"
                    value={editValue}
                    onChange={(e) => setEditValue(e.target.value)}
                    placeholder={`Enter ${field.label.toLowerCase()}`}
                    disabled={loading}
                    autoFocus
                    style={{ display: 'block' }}
                  />
                  <div className="profile-field-actions">
                    <button
                      className="bubble-btn"
                      onClick={() => handleSaveField(field.key)}
                      disabled={loading}
                      style={{ flex: 1, padding: '8px' }}
                    >
                      {loading ? '...' : '✓ Save'}
                    </button>
                    <button
                      className="bubble-btn-secondary"
                      onClick={handleCancel}
                      disabled={loading}
                      style={{ flex: 1, padding: '8px' }}
                    >
                      ✕ Cancel
                    </button>
                  </div>
                </div>
              ) : (
                <>
                  <div
                    className="profile-field-value editable"
                    onClick={() => handleEditField(field.key, userData.profile[field.key])}
                  >
                    {userData.profile[field.key] || `Click to add ${field.label.toLowerCase()}`}
                  </div>
                  <small style={{ color: 'rgba(255, 255, 255, 0.4)', marginTop: '4px', display: 'block' }}>
                    Click to edit
                  </small>
                </>
              )}
            </div>
          ))}
        </div>

        <div className="profile-section">
          <h3 className="profile-section-title">🔐 Account Security</h3>
          <div className="profile-field">
            <label className="profile-field-label">🔒 Password</label>
            <div className="profile-field-value">••••••••••</div>
            <small style={{ color: 'rgba(255, 255, 255, 0.4)', marginTop: '4px', display: 'block' }}>
              Password changes are not available in demo mode
            </small>
          </div>
          <div className="profile-field">
            <label className="profile-field-label">📧 Email</label>
            <div className="profile-field-value">{userData.email}</div>
            <small style={{ color: 'rgba(255, 255, 255, 0.4)', marginTop: '4px', display: 'block' }}>
              Email changes are not available in demo mode
            </small>
          </div>
        </div>

        <div className="profile-actions">
          <button
            className="bubble-btn"
            onClick={() => navigate('/home')}
          >
            ← Back to Home
          </button>
          <button
            className="bubble-btn-secondary"
            onClick={handleLogout}
          >
            🚪 Logout
          </button>
        </div>
      </div>
    </div>
  );
};

export default Profile;
