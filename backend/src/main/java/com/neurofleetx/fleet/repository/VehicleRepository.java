package com.neurofleetx.fleet.repository;

import com.neurofleetx.fleet.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);
    List<Vehicle> findByStatus(String status);
    List<Vehicle> findByVehicleType(String vehicleType);

    @Query("SELECT v FROM Vehicle v WHERE v.status = ?1")
    List<Vehicle> findAllByStatus(String status);
}
