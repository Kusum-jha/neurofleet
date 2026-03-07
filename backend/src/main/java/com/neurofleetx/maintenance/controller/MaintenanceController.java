package com.neurofleetx.maintenance.controller;

import com.neurofleetx.common.dto.ApiResponse;
import com.neurofleetx.maintenance.dto.MaintenanceAlertDTO;
import com.neurofleetx.maintenance.dto.MaintenanceRecordDTO;
import com.neurofleetx.maintenance.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/maintenance")
@CrossOrigin(origins = "http://localhost:5173")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    // Maintenance Records Endpoints

    @PostMapping("/records")
    public ResponseEntity<ApiResponse<MaintenanceRecordDTO>> createMaintenanceRecord(
            @RequestBody MaintenanceRecordDTO dto) {
        MaintenanceRecordDTO created = maintenanceService.createMaintenanceRecord(dto);
        return ResponseEntity.ok(ApiResponse.success(created), "Maintenance record created successfully");
    }

    @GetMapping("/records")
    public ResponseEntity<ApiResponse<List<MaintenanceRecordDTO>>> getAllMaintenanceRecords() {
        List<MaintenanceRecordDTO> records = maintenanceService.getAllMaintenanceRecords();
        return ResponseEntity.ok(ApiResponse.success(records), "Retrieved all maintenance records");
    }

    @GetMapping("/records/vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<List<MaintenanceRecordDTO>>> getMaintenanceByVehicle(
            @PathVariable Long vehicleId) {
        List<MaintenanceRecordDTO> records = maintenanceService.getMaintenanceByVehicle(vehicleId);
        return ResponseEntity.ok(ApiResponse.success(records), "Retrieved maintenance records for vehicle");
    }

    @GetMapping("/records/status/{status}")
    public ResponseEntity<ApiResponse<List<MaintenanceRecordDTO>>> getMaintenanceByStatus(
            @PathVariable String status) {
        List<MaintenanceRecordDTO> records = maintenanceService.getMaintenanceByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(records), "Retrieved maintenance records by status");
    }

    @GetMapping("/records/scheduled")
    public ResponseEntity<ApiResponse<List<MaintenanceRecordDTO>>> getScheduledMaintenance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<MaintenanceRecordDTO> records = maintenanceService.getScheduledMaintenance(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(records), "Retrieved scheduled maintenance");
    }

    @PutMapping("/records/{id}")
    public ResponseEntity<ApiResponse<MaintenanceRecordDTO>> updateMaintenanceRecord(
            @PathVariable Long id,
            @RequestBody MaintenanceRecordDTO dto) {
        MaintenanceRecordDTO updated = maintenanceService.updateMaintenanceRecord(id, dto);
        if (updated == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Maintenance record not found", 400));
        }
        return ResponseEntity.ok(ApiResponse.success(updated), "Maintenance record updated successfully");
    }

    @PutMapping("/records/{id}/complete")
    public ResponseEntity<ApiResponse<MaintenanceRecordDTO>> completeMaintenanceRecord(
            @PathVariable Long id) {
        MaintenanceRecordDTO completed = maintenanceService.completeMaintenanceRecord(id);
        if (completed == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Maintenance record not found", 400));
        }
        return ResponseEntity.ok(ApiResponse.success(completed), "Maintenance record completed");
    }

    // Alerts Endpoints

    @PostMapping("/alerts/generate")
    public ResponseEntity<ApiResponse<String>> generateAlerts() {
        maintenanceService.generateMaintenanceAlerts();
        return ResponseEntity.ok(ApiResponse.success("Success"), "Maintenance alerts generated");
    }

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<MaintenanceAlertDTO>>> getAllAlerts() {
        List<MaintenanceAlertDTO> alerts = maintenanceService.getAllUnresolvedAlerts();
        return ResponseEntity.ok(ApiResponse.success(alerts), "Retrieved all unresolved alerts");
    }

    @GetMapping("/alerts/vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<List<MaintenanceAlertDTO>>> getAlertsForVehicle(
            @PathVariable Long vehicleId) {
        List<MaintenanceAlertDTO> alerts = maintenanceService.getAlertsForVehicle(vehicleId);
        return ResponseEntity.ok(ApiResponse.success(alerts), "Retrieved alerts for vehicle");
    }

    @PutMapping("/alerts/{alertId}/resolve")
    public ResponseEntity<ApiResponse<MaintenanceAlertDTO>> resolveAlert(
            @PathVariable Long alertId) {
        MaintenanceAlertDTO resolved = maintenanceService.resolveAlert(alertId);
        if (resolved == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Alert not found", 400));
        }
        return ResponseEntity.ok(ApiResponse.success(resolved), "Alert resolved");
    }

    // Statistics & Prediction Endpoints

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMaintenanceStatistics() {
        Map<String, Object> stats = maintenanceService.getMaintenanceStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats), "Retrieved maintenance statistics");
    }

    @GetMapping("/predict/{vehicleId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> predictMaintenanceNeeds(
            @PathVariable Long vehicleId) {
        Map<String, Object> prediction = maintenanceService.predictMaintenanceNeeds(vehicleId);
        if (prediction == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Vehicle not found", 400));
        }
        return ResponseEntity.ok(ApiResponse.success(prediction), "Predictive maintenance analysis");
    }
}
