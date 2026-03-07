package com.neurofleetx.fleet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "telemetry")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Telemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(columnDefinition = "DECIMAL(10,8)")
    private BigDecimal latitude;

    @Column(columnDefinition = "DECIMAL(11,8)")
    private BigDecimal longitude;

    @Column(name = "speed_kmh")
    private Integer speedKmh;

    @Column(name = "battery_percentage")
    private Integer batteryPercentage;

    @Column(name = "fuel_percentage")
    private Integer fuelPercentage;

    @Column(name = "temperature_celsius")
    private Integer temperatureCelsius;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @PrePersist
    protected void onCreate() {
        if (recordedAt == null) {
            recordedAt = LocalDateTime.now();
        }
    }
}
