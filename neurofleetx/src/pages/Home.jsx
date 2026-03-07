import { useNavigate } from 'react-router-dom';
import { getCurrentUser } from '../utils/auth';
import '../styles/pages.css';

const Home = () => {
  const navigate = useNavigate();
  const user = getCurrentUser();

  return (
    <div className="home-container">
      <div className="home-header">
        <h1 className="home-title">Welcome to NeuroFleetX</h1>
        <p className="home-subtitle">
          {user ? `Hello, ${user.username}! 👋` : 'The Next Generation AI Fleet Management'}
        </p>
      </div>

      <div className="button-group">
        <button className="bubble-btn" onClick={() => navigate('/profile')}>
          👤 View Profile
        </button>
        <button className="bubble-btn-secondary" onClick={() => window.location.reload()}>
          🔄 Refresh
        </button>
      </div>

      <div className="feature-grid">
        <div className="glass-card feature-card">
          <h3>🚀 Lightning Fast</h3>
          <p>
            Experience blazing-fast performance with our optimized infrastructure
            and cutting-edge technology stack.
          </p>
        </div>

        <div className="glass-card feature-card">
          <h3>🔐 Secure & Private</h3>
          <p>
            Your data is encrypted and stored securely. We prioritize your privacy
            with enterprise-grade security measures.
          </p>
        </div>

        <div className="glass-card feature-card">
          <h3>✨ Beautiful Design</h3>
          <p>
            Enjoy a modern, glossy glassmorphism UI with smooth animations and
            intuitive navigation designed for you.
          </p>
        </div>

        <div className="glass-card feature-card">
          <h3>📊 Real-time Analytics</h3>
          <p>
            Get instant insights with our real-time analytics dashboard and
            comprehensive reporting tools.
          </p>
        </div>

        <div className="glass-card feature-card">
          <h3>🤖 AI-Powered</h3>
          <p>
            Leverage artificial intelligence to automate tasks, predict trends,
            and optimize your fleet management.
          </p>
        </div>

        <div className="glass-card feature-card">
          <h3>🌐 Cloud Native</h3>
          <p>
            Built for the cloud with seamless scalability, reliability, and
            automatic updates across all devices.
          </p>
        </div>
      </div>

      <div style={{ marginTop: '60px', textAlign: 'center' }}>
        <p style={{ color: 'rgba(255, 255, 255, 0.6)', fontSize: '14px' }}>
          👉 Ready to manage your fleet? Visit your{' '}
          <span
            onClick={() => navigate('/profile')}
            style={{
              color: 'var(--ocean-lighter)',
              curser: 'pointer',
              textDecoration: 'underline',
              fontWeight: 'bold'
            }}
          >
            profile
          </span>
          {' '}to get started.
        </p>
      </div>
    </div>
  );
};

export default Home;
