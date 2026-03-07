import '../styles/dashboard.css';

const DashboardMetrics = ({ metrics }) => {
  if (!metrics || Object.keys(metrics).length === 0) {
    return <div className="metrics-grid">Loading metrics...</div>;
  }

  return (
    <div className="metrics-grid">
      {Object.entries(metrics).map(([key, value]) => (
        <div key={key} className="metric-card glass-effect">
          <div className="metric-icon">📊</div>
          <div className="metric-content">
            <p className="metric-label">{formatLabel(key)}</p>
            <p className="metric-value">{formatValue(value)}</p>
          </div>
        </div>
      ))}
    </div>
  );
};

const formatLabel = (label) => {
  return label
    .replace(/([A-Z])/g, ' $1')
    .replace(/^./, (str) => str.toUpperCase())
    .trim();
};

const formatValue = (value) => {
  if (typeof value === 'number') {
    if (value < 100 && value % 1 !== 0) {
      return value.toFixed(1);
    }
    return value;
  }
  return value;
};

export default DashboardMetrics;
