package com.neurofleetx.route.repository;

import com.neurofleetx.route.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByVehicleId(Long vehicleId);
    List<Route> findByStatus(String status);
    List<Route> findByVehicleIdAndStatus(Long vehicleId, String status);
}
