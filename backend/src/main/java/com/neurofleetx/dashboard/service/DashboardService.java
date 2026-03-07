package com.neurofleetx.dashboard.service;

import com.neurofleetx.auth.repository.UserRepository;
import com.neurofleetx.fleet.repository.VehicleRepository;
import com.neurofleetx.route.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final RouteRepository routeRepository;

    public Map<String, Object> getAdminMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalUsers", userRepository.count());
        metrics.put("totalVehicles", vehicleRepository.count());
        metrics.put("totalRoutes", routeRepository.count());
        metrics.put("activeRoutes", routeRepository.findByStatus("IN_PROGRESS").size());
        metrics.put("completedRoutes", routeRepository.findByStatus("COMPLETED").size());
        metrics.put("availableVehicles", vehicleRepository.findByStatus("AVAILABLE").size());
        metrics.put("busyVehicles", vehicleRepository.findByStatus("IN_USE").size());
        metrics.put("maintenanceVehicles", vehicleRepository.findByStatus("MAINTENANCE").size());
        return metrics;
    }

    public Map<String, Object> getManagerMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalVehicles", vehicleRepository.count());
        metrics.put("availableVehicles", vehicleRepository.findByStatus("AVAILABLE").size());
        metrics.put("activeRoutes", routeRepository.findByStatus("IN_PROGRESS").size());
        metrics.put("plannedRoutes", routeRepository.findByStatus("PLANNED").size());
        metrics.put("completedToday", routeRepository.findByStatus("COMPLETED").size());
        metrics.put("maintenanceVehicles", vehicleRepository.findByStatus("MAINTENANCE").size());
        metrics.put("fleetUtilization", calculateFleetUtilization());
        return metrics;
    }

    public Map<String, Object> getDriverMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("assignedRoutes", routeRepository.count());
        metrics.put("completedRoutes", routeRepository.findByStatus("COMPLETED").size());
        metrics.put("activeRoutes", routeRepository.findByStatus("IN_PROGRESS").size());
        metrics.put("earnings", calculateEarnings());
        metrics.put("averageRating", 4.8);
        metrics.put("tripsToday", countTripsToday());
        return metrics;
    }

    public Map<String, Object> getCustomerMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("upcomingBookings", 3);
        metrics.put("completedBookings", 12);
        metrics.put("totalSpent", 4500.0);
        metrics.put("averageRideTime", 45);
        metrics.put("favoriteVehicleType", "Electric");
        metrics.put("loyaltyPoints", 1250);
        return metrics;
    }

    private double calculateFleetUtilization() {
        long total = vehicleRepository.count();
        if (total == 0) return 0;
        long inUse = vehicleRepository.findByStatus("IN_USE").size();
        return (inUse * 100.0) / total;
    }

    private double calculateEarnings() {
        return 2500.0; // Mock value
    }

    private int countTripsToday() {
        return 8; // Mock value
    }
}
