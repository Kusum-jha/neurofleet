package com.neurofleetx.fleet.controller;

import com.neurofleetx.fleet.dto.TelemetryDTO;
import com.neurofleetx.fleet.service.TelemetryService;
import com.neurofleetx.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/telemetry")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class TelemetryController {

    private final TelemetryService telemetryService;

    @GetMapping("/vehicles/{vehicleId}/latest")
    public ResponseEntity<ApiResponse<TelemetryDTO>> getLatestTelemetry(@PathVariable Long vehicleId) {
        TelemetryDTO telemetry = telemetryService.getLatestTelemetry(vehicleId);
        if (telemetry == null) {
            return ResponseEntity.ok(ApiResponse.success(null, "No telemetry data available"));
        }
        return ResponseEntity.ok(ApiResponse.success(telemetry, "Latest telemetry retrieved"));
    }

    @GetMapping("/vehicles/{vehicleId}/history")
    public ResponseEntity<ApiResponse<List<TelemetryDTO>>> getTelemetryHistory(
            @PathVariable Long vehicleId,
            @RequestParam(defaultValue = "24") int hours) {
        List<TelemetryDTO> telemetry = telemetryService.getTelemetryHistory(vehicleId, hours);
        return ResponseEntity.ok(ApiResponse.success(telemetry, "Telemetry history retrieved"));
    }

    @PostMapping("/vehicles/{vehicleId}/record")
    public ResponseEntity<ApiResponse<TelemetryDTO>> recordTelemetry(
            @PathVariable Long vehicleId,
            @RequestBody TelemetryDTO dto) {
        TelemetryDTO recorded = telemetryService.recordTelemetry(vehicleId, dto);
        if (recorded == null) {
            return ResponseEntity.status(404).body(ApiResponse.error("Vehicle not found", 404));
        }
        return ResponseEntity.ok(ApiResponse.success(recorded, "Telemetry recorded successfully"));
    }

    @PostMapping("/vehicles/{vehicleId}/simulate")
    public ResponseEntity<ApiResponse<TelemetryDTO>> simulateTelemetry(@PathVariable Long vehicleId) {
        TelemetryDTO simulated = telemetryService.simulateTelemetry(vehicleId);
        if (simulated == null) {
            return ResponseEntity.status(404).body(ApiResponse.error("Vehicle not found", 404));
        }
        return ResponseEntity.ok(ApiResponse.success(simulated, "Telemetry simulated successfully"));
    }
}
