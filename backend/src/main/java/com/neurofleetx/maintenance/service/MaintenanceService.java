package com.neurofleetx.maintenance.service;

import com.neurofleetx.fleet.entity.Vehicle;
import com.neurofleetx.fleet.repository.VehicleRepository;
import com.neurofleetx.maintenance.dto.MaintenanceAlertDTO;
import com.neurofleetx.maintenance.dto.MaintenanceRecordDTO;
import com.neurofleetx.maintenance.entity.MaintenanceAlert;
import com.neurofleetx.maintenance.entity.MaintenanceRecord;
import com.neurofleetx.maintenance.repository.MaintenanceAlertRepository;
import com.neurofleetx.maintenance.repository.MaintenanceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRecordRepository maintenanceRecordRepository;

    @Autowired
    private MaintenanceAlertRepository maintenanceAlertRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    /**
     * Create a new maintenance record
     */
    public MaintenanceRecordDTO createMaintenanceRecord(MaintenanceRecordDTO dto) {
        MaintenanceRecord record = new MaintenanceRecord();
        record.setMaintenanceType(dto.getMaintenanceType());
        record.setDescription(dto.getDescription());
        record.setStatus(dto.getStatus() != null ? dto.getStatus() : "SCHEDULED");
        record.setScheduledDate(dto.getScheduledDate());
        record.setCost(dto.getCost());
        record.setNotes(dto.getNotes());

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId()).orElse(null);
        record.setVehicle(vehicle);

        MaintenanceRecord saved = maintenanceRecordRepository.save(record);
        return convertToDTO(saved);
    }

    /**
     * Get all maintenance records
     */
    public List<MaintenanceRecordDTO> getAllMaintenanceRecords() {
        return maintenanceRecordRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get maintenance records for a specific vehicle
     */
    public List<MaintenanceRecordDTO> getMaintenanceByVehicle(Long vehicleId) {
        return maintenanceRecordRepository.findByVehicleIdOrderByScheduledDateDesc(vehicleId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get maintenance records by status
     */
    public List<MaintenanceRecordDTO> getMaintenanceByStatus(String status) {
        return maintenanceRecordRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get scheduled maintenance for a date range
     */
    public List<MaintenanceRecordDTO> getScheduledMaintenance(LocalDate startDate, LocalDate endDate) {
        return maintenanceRecordRepository.findScheduledBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update maintenance record
     */
    public MaintenanceRecordDTO updateMaintenanceRecord(Long id, MaintenanceRecordDTO dto) {
        MaintenanceRecord record = maintenanceRecordRepository.findById(id).orElse(null);
        if (record == null) {
            return null;
        }

        if (dto.getMaintenanceType() != null) {
            record.setMaintenanceType(dto.getMaintenanceType());
        }
        if (dto.getDescription() != null) {
            record.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            record.setStatus(dto.getStatus());
        }
        if (dto.getScheduledDate() != null) {
            record.setScheduledDate(dto.getScheduledDate());
        }
        if (dto.getCompletedDate() != null) {
            record.setCompletedDate(dto.getCompletedDate());
        }
        if (dto.getCost() != null) {
            record.setCost(dto.getCost());
        }
        if (dto.getNotes() != null) {
            record.setNotes(dto.getNotes());
        }

        MaintenanceRecord updated = maintenanceRecordRepository.save(record);
        return convertToDTO(updated);
    }

    /**
     * Complete maintenance record
     */
    public MaintenanceRecordDTO completeMaintenanceRecord(Long id) {
        MaintenanceRecord record = maintenanceRecordRepository.findById(id).orElse(null);
        if (record == null) {
            return null;
        }

        record.setStatus("COMPLETED");
        record.setCompletedDate(LocalDate.now());

        // Update vehicle's last maintenance date
        if (record.getVehicle() != null) {
            record.getVehicle().setLastMaintenance(LocalDate.now());
            vehicleRepository.save(record.getVehicle());
        }

        MaintenanceRecord updated = maintenanceRecordRepository.save(record);
        return convertToDTO(updated);
    }

    /**
     * Check and generate maintenance alerts for all vehicles
     */
    public void generateMaintenanceAlerts() {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        for (Vehicle vehicle : vehicles) {
            checkAndCreateAlerts(vehicle);
        }
    }

    /**
     * Check and create alerts for a specific vehicle
     */
    public void checkAndCreateAlerts(Vehicle vehicle) {
        // Check battery level
        if (vehicle.getBatteryPercentage() != null && vehicle.getBatteryPercentage() < 20) {
            createAlertIfNotExists(vehicle, "LOW_BATTERY", "MEDIUM", 
                "Battery level is low: " + vehicle.getBatteryPercentage() + "%");
        }

        // Check fuel level
        if (vehicle.getFuelPercentage() != null && vehicle.getFuelPercentage() < 20) {
            createAlertIfNotExists(vehicle, "LOW_FUEL", "MEDIUM",
                "Fuel level is low: " + vehicle.getFuelPercentage() + "%");
        }

        // Check mileage for maintenance
        if (vehicle.getMileageKm() != null && vehicle.getMileageKm() > 50000) {
            createAlertIfNotExists(vehicle, "HIGH_MILEAGE", "HIGH",
                "Vehicle mileage exceeds 50,000 km: " + vehicle.getMileageKm() + " km");
        }

        // Check overdue maintenance (if last maintenance was more than 6 months ago)
        if (vehicle.getLastMaintenance() != null) {
            LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
            if (vehicle.getLastMaintenance().isBefore(sixMonthsAgo)) {
                createAlertIfNotExists(vehicle, "OVERDUE_MAINTENANCE", "CRITICAL",
                    "Maintenance is overdue. Last maintenance: " + vehicle.getLastMaintenance());
            }
        }
    }

    /**
     * Create alert if it doesn't already exist (to avoid duplicates)
     */
    private void createAlertIfNotExists(Vehicle vehicle, String alertType, String severity, String message) {
        List<MaintenanceAlert> existingAlerts = maintenanceAlertRepository
                .findByVehicleIdAndResolvedFalseOrderByCreatedAtDesc(vehicle.getId());

        boolean alertExists = existingAlerts.stream()
                .anyMatch(alert -> alert.getAlertType().equals(alertType) && !alert.getResolved());

        if (!alertExists) {
            MaintenanceAlert alert = new MaintenanceAlert();
            alert.setVehicle(vehicle);
            alert.setAlertType(alertType);
            alert.setSeverity(severity);
            alert.setMessage(message);
            alert.setResolved(false);
            maintenanceAlertRepository.save(alert);
        }
    }

    /**
     * Get all unresolved alerts
     */
    public List<MaintenanceAlertDTO> getAllUnresolvedAlerts() {
        return maintenanceAlertRepository.findByResolvedFalseOrderByCreatedAtDesc().stream()
                .map(this::convertAlertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get unresolved alerts for a vehicle
     */
    public List<MaintenanceAlertDTO> getAlertsForVehicle(Long vehicleId) {
        return maintenanceAlertRepository.findByVehicleIdAndResolvedFalseOrderByCreatedAtDesc(vehicleId).stream()
                .map(this::convertAlertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Resolve an alert
     */
    public MaintenanceAlertDTO resolveAlert(Long alertId) {
        MaintenanceAlert alert = maintenanceAlertRepository.findById(alertId).orElse(null);
        if (alert == null) {
            return null;
        }

        alert.setResolved(true);
        alert.setResolvedAt(LocalDateTime.now());
        MaintenanceAlert updated = maintenanceAlertRepository.save(alert);
        return convertAlertToDTO(updated);
    }

    /**
     * Get maintenance statistics
     */
    public Map<String, Object> getMaintenanceStatistics() {
        List<MaintenanceRecord> allRecords = maintenanceRecordRepository.findAll();
        List<MaintenanceAlert> allAlerts = maintenanceAlertRepository.findAll();

        int totalScheduled = (int) allRecords.stream()
                .filter(r -> "SCHEDULED".equals(r.getStatus())).count();
        int totalCompleted = (int) allRecords.stream()
                .filter(r -> "COMPLETED".equals(r.getStatus())).count();
        int totalInProgress = (int) allRecords.stream()
                .filter(r -> "IN_PROGRESS".equals(r.getStatus())).count();
        int unresolvedAlerts = (int) allAlerts.stream()
                .filter(alert -> !alert.getResolved()).count();

        double totalCost = allRecords.stream()
                .filter(r -> "COMPLETED".equals(r.getStatus()) && r.getCost() != null)
                .mapToDouble(MaintenanceRecord::getCost)
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalScheduled", totalScheduled);
        stats.put("totalCompleted", totalCompleted);
        stats.put("totalInProgress", totalInProgress);
        stats.put("unresolvedAlerts", unresolvedAlerts);
        stats.put("totalMaintenanceCost", totalCost);

        return stats;
    }

    /**
     * Predict maintenance needs based on vehicle telemetry
     */
    public Map<String, Object> predictMaintenanceNeeds(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
        if (vehicle == null) {
            return null;
        }

        Map<String, Object> prediction = new HashMap<>();
        List<String> recommendedActions = new ArrayList<>();

        // Check battery health
        if (vehicle.getBatteryPercentage() != null) {
            if (vehicle.getBatteryPercentage() < 30) {
                recommendedActions.add("Battery replacement recommended");
                prediction.put("batteryHealth", "POOR");
            } else if (vehicle.getBatteryPercentage() < 50) {
                recommendedActions.add("Battery inspection recommended");
                prediction.put("batteryHealth", "FAIR");
            } else {
                prediction.put("batteryHealth", "GOOD");
            }
        }

        // Check mileage
        if (vehicle.getMileageKm() != null) {
            if (vehicle.getMileageKm() > 70000) {
                recommendedActions.add("Major service required");
                prediction.put("serviceType", "MAJOR");
            } else if (vehicle.getMileageKm() > 40000) {
                recommendedActions.add("Regular service recommended");
                prediction.put("serviceType", "REGULAR");
            } else {
                prediction.put("serviceType", "MINOR");
            }
        }

        prediction.put("recommendedActions", recommendedActions);
        prediction.put("nextServiceDue", LocalDate.now().plusMonths(3));

        return prediction;
    }

    // Helper methods
    private MaintenanceRecordDTO convertToDTO(MaintenanceRecord record) {
        return new MaintenanceRecordDTO(
                record.getId(),
                record.getVehicle() != null ? record.getVehicle().getId() : null,
                record.getVehicle() != null ? record.getVehicle().getVehicleNumber() : null,
                record.getMaintenanceType(),
                record.getDescription(),
                record.getStatus(),
                record.getScheduledDate(),
                record.getCompletedDate(),
                record.getCost(),
                record.getNotes(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }

    private MaintenanceAlertDTO convertAlertToDTO(MaintenanceAlert alert) {
        return new MaintenanceAlertDTO(
                alert.getId(),
                alert.getVehicle() != null ? alert.getVehicle().getId() : null,
                alert.getVehicle() != null ? alert.getVehicle().getVehicleNumber() : null,
                alert.getAlertType(),
                alert.getSeverity(),
                alert.getMessage(),
                alert.getResolved(),
                alert.getResolvedAt(),
                alert.getCreatedAt()
        );
    }
}
