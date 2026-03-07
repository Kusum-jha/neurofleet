package com.neurofleetx.fleet.repository;

import com.neurofleetx.fleet.entity.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TelemetryRepository extends JpaRepository<Telemetry, Long> {

    @Query("SELECT t FROM Telemetry t WHERE t.vehicle.id = ?1 ORDER BY t.recordedAt DESC LIMIT 1")
    Optional<Telemetry> findLatestByVehicleId(Long vehicleId);

    @Query("SELECT t FROM Telemetry t WHERE t.vehicle.id = ?1 AND t.recordedAt >= ?2 ORDER BY t.recordedAt DESC")
    List<Telemetry> findByVehicleIdAndRecordedAfter(Long vehicleId, LocalDateTime dateTime);

    List<Telemetry> findByVehicleId(Long vehicleId);
}
