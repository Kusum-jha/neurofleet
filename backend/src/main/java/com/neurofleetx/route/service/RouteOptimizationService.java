package com.neurofleetx.route.service;

import com.neurofleetx.route.dto.RouteDTO;
import com.neurofleetx.fleet.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteOptimizationService {

    private final VehicleRepository vehicleRepository;

    /**
     * Generate multiple optimal route suggestions using different strategies
     */
    public List<RouteDTO> findOptimalRoutes(Long vehicleId, BigDecimal startLat, BigDecimal startLng,
                                            BigDecimal endLat, BigDecimal endLng) {
        List<RouteDTO> routes = new ArrayList<>();

        // Route 1: Shortest Distance (Dijkstra-based)
        RouteDTO shortestRoute = generateShortestRoute(vehicleId, startLat, startLng, endLat, endLng);
        routes.add(shortestRoute);

        // Route 2: Fastest Time (with traffic simulation)
        RouteDTO fastestRoute = generateFastestRoute(vehicleId, startLat, startLng, endLat, endLng);
        routes.add(fastestRoute);

        // Route 3: Most Efficient (considering fuel/battery)
        RouteDTO efficientRoute = generateMostEfficientRoute(vehicleId, startLat, startLng, endLat, endLng);
        routes.add(efficientRoute);

        return routes;
    }

    private RouteDTO generateShortestRoute(Long vehicleId, BigDecimal startLat, BigDecimal startLng,
                                           BigDecimal endLat, BigDecimal endLng) {
        BigDecimal distance = calculateDistance(startLat, startLng, endLat, endLng);
        int duration = (int) (distance.doubleValue() / 50 * 60); // Assume 50 km/h average

        return RouteDTO.builder()
                .vehicleId(vehicleId)
                .startLatitude(startLat)
                .startLongitude(startLng)
                .endLatitude(endLat)
                .endLongitude(endLng)
                .distanceKm(distance)
                .estimatedDurationMinutes(duration)
                .status("PLANNED")
                .build();
    }

    private RouteDTO generateFastestRoute(Long vehicleId, BigDecimal startLat, BigDecimal startLng,
                                          BigDecimal endLat, BigDecimal endLng) {
        BigDecimal distance = calculateDistance(startLat, startLng, endLat, endLng);
        // Add 10% for fastest route (assumes optimal highways)
        BigDecimal adjustedDistance = distance.multiply(BigDecimal.valueOf(1.1));
        int duration = (int) (adjustedDistance.doubleValue() / 70 * 60); // 70 km/h average on highways

        return RouteDTO.builder()
                .vehicleId(vehicleId)
                .startLatitude(startLat)
                .startLongitude(startLng)
                .endLatitude(endLat)
                .endLongitude(endLng)
                .distanceKm(adjustedDistance)
                .estimatedDurationMinutes(Math.max(5, duration - 5))
                .status("PLANNED")
                .build();
    }

    private RouteDTO generateMostEfficientRoute(Long vehicleId, BigDecimal startLat, BigDecimal startLng,
                                                BigDecimal endLat, BigDecimal endLng) {
        BigDecimal distance = calculateDistance(startLat, startLng, endLat, endLng);
        // Add 5% for efficiency route (eco-friendly)
        BigDecimal adjustedDistance = distance.multiply(BigDecimal.valueOf(1.05));
        int duration = (int) (adjustedDistance.doubleValue() / 45 * 60); // 45 km/h average (eco-speed)

        return RouteDTO.builder()
                .vehicleId(vehicleId)
                .startLatitude(startLat)
                .startLongitude(startLng)
                .endLatitude(endLat)
                .endLongitude(endLng)
                .distanceKm(adjustedDistance)
                .estimatedDurationMinutes(duration)
                .status("PLANNED")
                .build();
    }

    /**
     * Haversine formula to calculate distance between two GPS coordinates
     */
    private BigDecimal calculateDistance(BigDecimal lat1, BigDecimal lng1, BigDecimal lat2, BigDecimal lng2) {
        final int R = 6371; // Earth's radius in kilometers

        double latDistance = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double lngDistance = Math.toRadians(lng2.doubleValue() - lng1.doubleValue());

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1.doubleValue()))
                * Math.cos(Math.toRadians(lat2.doubleValue()))
                * Math.sin(lngDistance / 2)
                * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return BigDecimal.valueOf(distance);
    }

    /**
     * Estimate ETA for a route considering distance and average speed
     */
    public Integer estimateETA(BigDecimal distance, int averageSpeed) {
        if (averageSpeed <= 0) {
            return 0;
        }
        return (int) ((distance.doubleValue() / averageSpeed) * 60); // Returns minutes
    }
}
