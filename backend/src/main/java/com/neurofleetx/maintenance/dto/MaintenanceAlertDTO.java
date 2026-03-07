package com.neurofleetx.maintenance.dto;

import java.time.LocalDateTime;

public class MaintenanceAlertDTO {
    private Long id;
    private Long vehicleId;
    private String vehicleNumber;
    private String alertType;
    private String severity;
    private String message;
    private Boolean resolved;
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt;

    public MaintenanceAlertDTO() {}

    public MaintenanceAlertDTO(Long id, Long vehicleId, String vehicleNumber, String alertType,
                             String severity, String message, Boolean resolved,
                             LocalDateTime resolvedAt, LocalDateTime createdAt) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.vehicleNumber = vehicleNumber;
        this.alertType = alertType;
        this.severity = severity;
        this.message = message;
        this.resolved = resolved;
        this.resolvedAt = resolvedAt;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
