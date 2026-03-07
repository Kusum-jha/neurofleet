package com.neurofleetx.route.service;

import com.neurofleetx.route.dto.RouteDTO;
import com.neurofleetx.route.entity.Route;
import com.neurofleetx.route.entity.RouteStop;
import com.neurofleetx.route.repository.RouteRepository;
import com.neurofleetx.fleet.repository.VehicleRepository;
import com.neurofleetx.fleet.entity.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteService {

    private final RouteRepository routeRepository;
    private final VehicleRepository vehicleRepository;
    private final RouteOptimizationService optimizationService;

    public RouteDTO createRoute(RouteDTO dto) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(dto.getVehicleId());
        if (vehicleOpt.isEmpty()) {
            return null;
        }

        Route route = Route.builder()
                .vehicle(vehicleOpt.get())
                .startLatitude(dto.getStartLatitude())
                .startLongitude(dto.getStartLongitude())
                .endLatitude(dto.getEndLatitude())
                .endLongitude(dto.getEndLongitude())
                .distanceKm(dto.getDistanceKm())
                .estimatedDurationMinutes(dto.getEstimatedDurationMinutes())
                .status("PLANNED")
                .build();

        Route saved = routeRepository.save(route);
        return mapToDTO(saved);
    }

    public RouteDTO getRouteById(Long id) {
        Optional<Route> routeOpt = routeRepository.findById(id);
        return routeOpt.map(this::mapToDTO).orElse(null);
    }

    public List<RouteDTO> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<RouteDTO> getRoutesByVehicle(Long vehicleId) {
        return routeRepository.findByVehicleId(vehicleId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<RouteDTO> getRoutesByStatus(String status) {
        return routeRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public RouteDTO updateRouteStatus(Long id, String status) {
        Optional<Route> routeOpt = routeRepository.findById(id);
        if (routeOpt.isEmpty()) {
            return null;
        }

        Route route = routeOpt.get();
        route.setStatus(status);
        if ("COMPLETED".equals(status)) {
            route.setCompletedAt(LocalDateTime.now());
        }

        Route updated = routeRepository.save(route);
        return mapToDTO(updated);
    }

    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }

    public List<RouteDTO> optimizeRoute(Long vehicleId, BigDecimal startLat, BigDecimal startLng,
                                        BigDecimal endLat, BigDecimal endLng) {
        return optimizationService.findOptimalRoutes(vehicleId, startLat, startLng, endLat, endLng);
    }

    private RouteDTO mapToDTO(Route route) {
        return RouteDTO.builder()
                .id(route.getId())
                .vehicleId(route.getVehicle().getId())
                .vehicleNumber(route.getVehicle().getVehicleNumber())
                .startLatitude(route.getStartLatitude())
                .startLongitude(route.getStartLongitude())
                .endLatitude(route.getEndLatitude())
                .endLongitude(route.getEndLongitude())
                .distanceKm(route.getDistanceKm())
                .estimatedDurationMinutes(route.getEstimatedDurationMinutes())
                .actualDurationMinutes(route.getActualDurationMinutes())
                .status(route.getStatus())
                .stops(route.getStops())
                .createdAt(route.getCreatedAt())
                .completedAt(route.getCompletedAt())
                .build();
    }
}
