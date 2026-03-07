package com.neurofleetx.fleet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String vehicleNumber;

    @Column(nullable = false)
    private String status; // AVAILABLE, IN_USE, MAINTENANCE

    private String vehicleType;
    private Integer capacity;
    private String fuelType;

    @Column(name = "battery_percentage")
    private Integer batteryPercentage;

    @Column(name = "fuel_percentage")
    private Integer fuelPercentage;

    @Column(name = "current_latitude", columnDefinition = "DECIMAL(10,8)")
    private BigDecimal currentLatitude;

    @Column(name = "current_longitude", columnDefinition = "DECIMAL(11,8)")
    private BigDecimal currentLongitude;

    @Column(name = "speed_kmh")
    private Integer speedKmh;

    @Column(name = "mileage_km")
    private Integer mileageKm;

    @Column(name = "last_maintenance")
    private LocalDate lastMaintenance;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "AVAILABLE";
        }
        if (speedKmh == null) {
            speedKmh = 0;
        }
        if (mileageKm == null) {
            mileageKm = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
