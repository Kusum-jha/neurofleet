package com.neurofleetx.maintenance.repository;

import com.neurofleetx.maintenance.entity.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
    List<MaintenanceRecord> findByVehicleId(Long vehicleId);
    List<MaintenanceRecord> findByStatus(String status);
    List<MaintenanceRecord> findByVehicleIdAndStatus(Long vehicleId, String status);

    @Query("SELECT m FROM MaintenanceRecord m WHERE m.scheduledDate >= :startDate AND m.scheduledDate <= :endDate")
    List<MaintenanceRecord> findScheduledBetween(@Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

    @Query("SELECT m FROM MaintenanceRecord m WHERE m.vehicle.id = :vehicleId ORDER BY m.scheduledDate DESC")
    List<MaintenanceRecord> findByVehicleIdOrderByScheduledDateDesc(@Param("vehicleId") Long vehicleId);
}
