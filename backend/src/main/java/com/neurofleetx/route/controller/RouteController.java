package com.neurofleetx.route.controller;

import com.neurofleetx.route.dto.RouteDTO;
import com.neurofleetx.route.service.RouteService;
import com.neurofleetx.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    public ResponseEntity<ApiResponse<RouteDTO>> createRoute(@RequestBody RouteDTO dto) {
        RouteDTO created = routeService.createRoute(dto);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Vehicle not found", 400));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Route created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RouteDTO>> getRouteById(@PathVariable Long id) {
        RouteDTO route = routeService.getRouteById(id);
        if (route == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Route not found", 404));
        }
        return ResponseEntity.ok(ApiResponse.success(route, "Route retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RouteDTO>>> getAllRoutes() {
        List<RouteDTO> routes = routeService.getAllRoutes();
        return ResponseEntity.ok(ApiResponse.success(routes, "Routes retrieved successfully"));
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<List<RouteDTO>>> getRoutesByVehicle(@PathVariable Long vehicleId) {
        List<RouteDTO> routes = routeService.getRoutesByVehicle(vehicleId);
        return ResponseEntity.ok(ApiResponse.success(routes, "Routes retrieved successfully"));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<RouteDTO>>> getRoutesByStatus(@PathVariable String status) {
        List<RouteDTO> routes = routeService.getRoutesByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(routes, "Routes retrieved successfully"));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<RouteDTO>> updateRouteStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        RouteDTO updated = routeService.updateRouteStatus(id, status);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Route not found", 404));
        }
        return ResponseEntity.ok(ApiResponse.success(updated, "Route status updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Route deleted successfully"));
    }

    @PostMapping("/optimize")
    public ResponseEntity<ApiResponse<List<RouteDTO>>> optimizeRoute(
            @RequestParam Long vehicleId,
            @RequestParam BigDecimal startLat,
            @RequestParam BigDecimal startLng,
            @RequestParam BigDecimal endLat,
            @RequestParam BigDecimal endLng) {
        List<RouteDTO> optimizedRoutes = routeService.optimizeRoute(vehicleId, startLat, startLng, endLat, endLng);
        return ResponseEntity.ok(ApiResponse.success(optimizedRoutes, "Route optimization completed"));
    }
}
