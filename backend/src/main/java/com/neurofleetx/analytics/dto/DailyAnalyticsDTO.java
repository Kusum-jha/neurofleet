package com.neurofleetx.analytics.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DailyAnalyticsDTO {
    private Long id;
    private LocalDate analyticsDate;
    private Integer totalBookings;
    private Integer completedBookings;
    private BigDecimal totalDistanceKm;
    private BigDecimal totalRevenue;
    private Integer activeVehicles;
    private Integer availableVehicles;
    private Integer maintenanceVehicles;
    private BigDecimal avgVehicleUtilization;

    public DailyAnalyticsDTO() {}

    public DailyAnalyticsDTO(Long id, LocalDate analyticsDate, Integer totalBookings,
                            Integer completedBookings, BigDecimal totalDistanceKm,
                            BigDecimal totalRevenue, Integer activeVehicles,
                            Integer availableVehicles, Integer maintenanceVehicles,
                            BigDecimal avgVehicleUtilization) {
        this.id = id;
        this.analyticsDate = analyticsDate;
        this.totalBookings = totalBookings;
        this.completedBookings = completedBookings;
        this.totalDistanceKm = totalDistanceKm;
        this.totalRevenue = totalRevenue;
        this.activeVehicles = activeVehicles;
        this.availableVehicles = availableVehicles;
        this.maintenanceVehicles = maintenanceVehicles;
        this.avgVehicleUtilization = avgVehicleUtilization;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAnalyticsDate() {
        return analyticsDate;
    }

    public void setAnalyticsDate(LocalDate analyticsDate) {
        this.analyticsDate = analyticsDate;
    }

    public Integer getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(Integer totalBookings) {
        this.totalBookings = totalBookings;
    }

    public Integer getCompletedBookings() {
        return completedBookings;
    }

    public void setCompletedBookings(Integer completedBookings) {
        this.completedBookings = completedBookings;
    }

    public BigDecimal getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public void setTotalDistanceKm(BigDecimal totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getActiveVehicles() {
        return activeVehicles;
    }

    public void setActiveVehicles(Integer activeVehicles) {
        this.activeVehicles = activeVehicles;
    }

    public Integer getAvailableVehicles() {
        return availableVehicles;
    }

    public void setAvailableVehicles(Integer availableVehicles) {
        this.availableVehicles = availableVehicles;
    }

    public Integer getMaintenanceVehicles() {
        return maintenanceVehicles;
    }

    public void setMaintenanceVehicles(Integer maintenanceVehicles) {
        this.maintenanceVehicles = maintenanceVehicles;
    }

    public BigDecimal getAvgVehicleUtilization() {
        return avgVehicleUtilization;
    }

    public void setAvgVehicleUtilization(BigDecimal avgVehicleUtilization) {
        this.avgVehicleUtilization = avgVehicleUtilization;
    }
}
