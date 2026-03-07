package com.neurofleetx.fleet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {
    private Long id;
    private String vehicleNumber;
    private String status;
    private String vehicleType;
    private Integer capacity;
    private String fuelType;
    private Integer batteryPercentage;
    private Integer fuelPercentage;
    private BigDecimal currentLatitude;
    private BigDecimal currentLongitude;
    private Integer speedKmh;
    private Integer mileageKm;
    private LocalDate lastMaintenance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
