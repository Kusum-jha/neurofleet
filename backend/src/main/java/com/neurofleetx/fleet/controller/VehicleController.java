package com.neurofleetx.fleet.controller;

import com.neurofleetx.fleet.dto.VehicleDTO;
import com.neurofleetx.fleet.service.VehicleService;
import com.neurofleetx.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<ApiResponse<VehicleDTO>> createVehicle(@RequestBody VehicleDTO dto) {
        VehicleDTO created = vehicleService.createVehicle(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Vehicle created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VehicleDTO>>> getAllVehicles() {
        List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(ApiResponse.success(vehicles, "Vehicles retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleDTO>> getVehicleById(@PathVariable Long id) {
        VehicleDTO vehicle = vehicleService.getVehicleById(id);
        if (vehicle == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Vehicle not found", 404));
        }
        return ResponseEntity.ok(ApiResponse.success(vehicle, "Vehicle retrieved successfully"));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<VehicleDTO>>> getVehiclesByStatus(@PathVariable String status) {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(vehicles, "Vehicles retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleDTO>> updateVehicle(@PathVariable Long id, @RequestBody VehicleDTO dto) {
        VehicleDTO updated = vehicleService.updateVehicle(id, dto);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Vehicle not found", 404));
        }
        return ResponseEntity.ok(ApiResponse.success(updated, "Vehicle updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Vehicle deleted successfully"));
    }

    @GetMapping("/fleet/status")
    public ResponseEntity<ApiResponse<Object>> getFleetStatus() {
        List<VehicleDTO> allVehicles = vehicleService.getAllVehicles();
        long available = allVehicles.stream().filter(v -> "AVAILABLE".equals(v.getStatus())).count();
        long inUse = allVehicles.stream().filter(v -> "IN_USE".equals(v.getStatus())).count();
        long maintenance = allVehicles.stream().filter(v -> "MAINTENANCE".equals(v.getStatus())).count();

        Object status = new Object() {
            public final long totalVehicles = allVehicles.size();
            public final long availableVehicles = available;
            public final long inUseVehicles = inUse;
            public final long maintenanceVehicles = maintenance;
        };

        return ResponseEntity.ok(ApiResponse.success(status, "Fleet status retrieved"));
    }
}
