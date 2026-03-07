package com.neurofleetx.analytics.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_analytics")
public class DailyAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate analyticsDate;

    @Column(nullable = false)
    private Integer totalBookings = 0;

    @Column(nullable = false)
    private Integer completedBookings = 0;

    @Column(nullable = false)
    private BigDecimal totalDistanceKm = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer activeVehicles = 0;

    @Column(nullable = false)
    private Integer availableVehicles = 0;

    @Column(nullable = false)
    private Integer maintenanceVehicles = 0;

    @Column(nullable = false)
    private BigDecimal avgVehicleUtilization = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
