package com.neurofleetx.fleet.service;

import com.neurofleetx.fleet.dto.VehicleDTO;
import com.neurofleetx.fleet.entity.Vehicle;
import com.neurofleetx.fleet.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleDTO createVehicle(VehicleDTO dto) {
        Vehicle vehicle = Vehicle.builder()
                .vehicleNumber(dto.getVehicleNumber())
                .status(dto.getStatus() != null ? dto.getStatus() : "AVAILABLE")
                .vehicleType(dto.getVehicleType())
                .capacity(dto.getCapacity())
                .fuelType(dto.getFuelType())
                .batteryPercentage(dto.getBatteryPercentage())
                .fuelPercentage(dto.getFuelPercentage())
                .currentLatitude(dto.getCurrentLatitude())
                .currentLongitude(dto.getCurrentLongitude())
                .speedKmh(dto.getSpeedKmh())
                .mileageKm(dto.getMileageKm())
                .lastMaintenance(dto.getLastMaintenance())
                .build();

        Vehicle saved = vehicleRepository.save(vehicle);
        return mapToDTO(saved);
    }

    public VehicleDTO updateVehicle(Long id, VehicleDTO dto) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(id);
        if (vehicleOpt.isEmpty()) {
            return null;
        }

        Vehicle vehicle = vehicleOpt.get();
        vehicle.setStatus(dto.getStatus());
        vehicle.setCurrentLatitude(dto.getCurrentLatitude());
        vehicle.setCurrentLongitude(dto.getCurrentLongitude());
        vehicle.setSpeedKmh(dto.getSpeedKmh());
        vehicle.setBatteryPercentage(dto.getBatteryPercentage());
        vehicle.setFuelPercentage(dto.getFuelPercentage());
        vehicle.setMileageKm(dto.getMileageKm());

        Vehicle updated = vehicleRepository.save(vehicle);
        return mapToDTO(updated);
    }

    public VehicleDTO getVehicleById(Long id) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(id);
        return vehicleOpt.map(this::mapToDTO).orElse(null);
    }

    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<VehicleDTO> getVehiclesByStatus(String status) {
        return vehicleRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    private VehicleDTO mapToDTO(Vehicle vehicle) {
        return VehicleDTO.builder()
                .id(vehicle.getId())
                .vehicleNumber(vehicle.getVehicleNumber())
                .status(vehicle.getStatus())
                .vehicleType(vehicle.getVehicleType())
                .capacity(vehicle.getCapacity())
                .fuelType(vehicle.getFuelType())
                .batteryPercentage(vehicle.getBatteryPercentage())
                .fuelPercentage(vehicle.getFuelPercentage())
                .currentLatitude(vehicle.getCurrentLatitude())
                .currentLongitude(vehicle.getCurrentLongitude())
                .speedKmh(vehicle.getSpeedKmh())
                .mileageKm(vehicle.getMileageKm())
                .lastMaintenance(vehicle.getLastMaintenance())
                .createdAt(vehicle.getCreatedAt())
                .updatedAt(vehicle.getUpdatedAt())
                .build();
    }
}
