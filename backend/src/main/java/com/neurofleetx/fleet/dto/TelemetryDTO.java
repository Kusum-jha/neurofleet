package com.neurofleetx.fleet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelemetryDTO {
    private Long id;
    private Long vehicleId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer speedKmh;
    private Integer batteryPercentage;
    private Integer fuelPercentage;
    private Integer temperatureCelsius;
    private LocalDateTime recordedAt;
}
