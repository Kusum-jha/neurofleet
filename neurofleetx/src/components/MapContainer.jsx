import { useEffect, useRef } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

const MapContainer = ({ routes, vehicles }) => {
  const mapRef = useRef(null);
  const mapInstance = useRef(null);

  useEffect(() => {
    if (!mapRef.current) return;

    // Initialize map centered on New Delhi
    mapInstance.current = L.map(mapRef.current).setView([28.6139, 77.209], 11);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors',
      maxZoom: 19,
      opacity: 0.85,
    }).addTo(mapInstance.current);

    return () => {
      if (mapInstance.current) {
        mapInstance.current.remove();
      }
    };
  }, []);

  useEffect(() => {
    if (!mapInstance.current) return;

    // Clear existing layers (except base map)
    mapInstance.current.eachLayer((layer) => {
      if (layer instanceof L.Marker || layer instanceof L.Polyline) {
        mapInstance.current.removeLayer(layer);
      }
    });

    // Add vehicle markers
    if (vehicles && vehicles.length > 0) {
      vehicles.forEach((vehicle) => {
        if (vehicle.currentLatitude && vehicle.currentLongitude) {
          const icon = L.divIcon({
            className: 'vehicle-marker',
            html: `
              <div style="
                background: linear-gradient(135deg, #0066cc, #00b4ff);
                color: white;
                border-radius: 50%;
                width: 40px;
                height: 40px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: bold;
                box-shadow: 0 2px 8px rgba(0,102,204,0.3);
              ">🚗</div>
            `,
            iconSize: [40, 40],
            iconAnchor: [20, 20],
          });

          L.marker([vehicle.currentLatitude, vehicle.currentLongitude], { icon })
            .bindPopup(`<strong>${vehicle.vehicleNumber}</strong><br/>Status: ${vehicle.status}`)
            .addTo(mapInstance.current);
        }
      });
    }

    // Add route polylines
    if (routes && routes.length > 0) {
      const colors = ['#0066cc', '#ff6b6b', '#4caf50'];
      routes.forEach((route, index) => {
        const latlngs = [
          [route.startLatitude, route.startLongitude],
          [route.endLatitude, route.endLongitude],
        ];

        L.polyline(latlngs, {
          color: colors[index % colors.length],
          weight: 3,
          opacity: 0.7,
          dashArray: '5, 5',
        }).addTo(mapInstance.current);

        // Add markers for start and end points
        L.circleMarker([route.startLatitude, route.startLongitude], {
          radius: 6,
          fillColor: colors[index % colors.length],
          color: white,
          weight: 2,
          opacity: 1,
          fillOpacity: 0.8,
        })
          .bindPopup('Start')
          .addTo(mapInstance.current);

        L.circleMarker([route.endLatitude, route.endLongitude], {
          radius: 6,
          fillColor: colors[index % colors.length],
          color: '#fff',
          weight: 2,
          opacity: 1,
          fillOpacity: 0.8,
        })
          .bindPopup('End')
          .addTo(mapInstance.current);
      });
    }
  }, [routes, vehicles]);

  return (
    <div ref={mapRef} style={{ width: '100%', height: '100%', borderRadius: '15px' }}>
      {!routes && !vehicles && (
        <div style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          height: '100%',
          color: '#666',
          fontSize: '1.1rem',
        }}>
          Loading map... 🗺️
        </div>
      )}
    </div>
  );
};

export default MapContainer;
