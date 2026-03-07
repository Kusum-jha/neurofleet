package com.neurofleetx.route.dto;

import com.neurofleetx.route.entity.RouteStop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteDTO {
    private Long id;
    private Long vehicleId;
    private String vehicleNumber;
    private BigDecimal startLatitude;
    private BigDecimal startLongitude;
    private BigDecimal endLatitude;
    private BigDecimal endLongitude;
    private BigDecimal distanceKm;
    private Integer estimatedDurationMinutes;
    private Integer actualDurationMinutes;
    private String status;
    private List<RouteStop> stops;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
