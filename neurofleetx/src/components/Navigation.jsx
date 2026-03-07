import { Link, useLocation, useNavigate } from 'react-router-dom';
import { isAuthenticated, logoutUser } from '../utils/auth';
import '../styles/pages.css';

const Navigation = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const authenticated = isAuthenticated();

  const handleLogout = () => {
    logoutUser();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-content">
        <Link to="/" className="navbar-brand">
          <div className="navbar-brand-icon">🧠</div>
          <span>NeuroFleetX</span>
        </Link>

        <div className="navbar-menu">
          {authenticated ? (
            <>
              <Link
                to="/home"
                className={location.pathname === '/home' ? 'active' : ''}
              >
                Home
              </Link>
              <Link
                to="/bookings"
                className={location.pathname === '/bookings' ? 'active' : ''}
              >
                Bookings
              </Link>
              <Link
                to="/maintenance"
                className={location.pathname === '/maintenance' ? 'active' : ''}
              >
                Maintenance
              </Link>
              <Link
                to="/analytics"
                className={location.pathname === '/analytics' ? 'active' : ''}
              >
                Analytics
              </Link>
              <Link
                to="/fleet"
                className={location.pathname === '/fleet' ? 'active' : ''}
              >
                Fleet
              </Link>
              <Link
                to="/routes"
                className={location.pathname === '/routes' ? 'active' : ''}
              >
                Routes
              </Link>
              <Link
                to="/profile"
                className={location.pathname === '/profile' ? 'active' : ''}
              >
                Profile
              </Link>
              <button onClick={handleLogout}>
                Logout
              </button>
            </>
          ) : (
            <>
              <Link
                to="/login"
                className={location.pathname === '/login' ? 'active' : ''}
              >
                Login
              </Link>
              <Link
                to="/register"
                className={location.pathname === '/register' ? 'active' : ''}
              >
                Register
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navigation;
