package com.neurofleetx.route.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "route_stops")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "stop_order", nullable = false)
    private Integer stopOrder;

    @Column(columnDefinition = "DECIMAL(10,8)")
    private BigDecimal latitude;

    @Column(columnDefinition = "DECIMAL(11,8)")
    private BigDecimal longitude;

    private String description;

    private LocalDateTime eta;

    @Column(name = "arrived_at")
    private LocalDateTime arrivedAt;
}
