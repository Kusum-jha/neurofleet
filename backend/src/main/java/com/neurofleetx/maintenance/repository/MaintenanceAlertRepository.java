package com.neurofleetx.maintenance.repository;

import com.neurofleetx.maintenance.entity.MaintenanceAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceAlertRepository extends JpaRepository<MaintenanceAlert, Long> {
    List<MaintenanceAlert> findByVehicleId(Long vehicleId);
    List<MaintenanceAlert> findByVehicleIdAndResolved(Long vehicleId, Boolean resolved);
    List<MaintenanceAlert> findBySeverity(String severity);
    List<MaintenanceAlert> findByResolvedFalseOrderByCreatedAtDesc();
    List<MaintenanceAlert> findByVehicleIdAndResolvedFalseOrderByCreatedAtDesc(Long vehicleId);
}
