package com.neurofleetx.route.entity;

import jakarta.persistence.*;
import com.neurofleetx.fleet.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "start_latitude", columnDefinition = "DECIMAL(10,8)")
    private BigDecimal startLatitude;

    @Column(name = "start_longitude", columnDefinition = "DECIMAL(11,8)")
    private BigDecimal startLongitude;

    @Column(name = "end_latitude", columnDefinition = "DECIMAL(10,8)")
    private BigDecimal endLatitude;

    @Column(name = "end_longitude", columnDefinition = "DECIMAL(11,8)")
    private BigDecimal endLongitude;

    @Column(name = "distance_km", columnDefinition = "DECIMAL(10,2)")
    private BigDecimal distanceKm;

    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;

    @Column(name = "actual_duration_minutes")
    private Integer actualDurationMinutes;

    @Column(nullable = false)
    private String status; // PLANNED, IN_PROGRESS, COMPLETED, CANCELLED

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RouteStop> stops;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = "PLANNED";
        }
    }
}
