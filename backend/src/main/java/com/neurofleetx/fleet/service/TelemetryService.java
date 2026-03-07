package com.neurofleetx.fleet.service;

import com.neurofleetx.fleet.dto.TelemetryDTO;
import com.neurofleetx.fleet.entity.Telemetry;
import com.neurofleetx.fleet.entity.Vehicle;
import com.neurofleetx.fleet.repository.TelemetryRepository;
import com.neurofleetx.fleet.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TelemetryService {

    private final TelemetryRepository telemetryRepository;
    private final VehicleRepository vehicleRepository;
    private final Random random = new Random();

    public TelemetryDTO getLatestTelemetry(Long vehicleId) {
        Optional<Telemetry> telemetry = telemetryRepository.findLatestByVehicleId(vehicleId);
        return telemetry.map(this::mapToDTO).orElse(null);
    }

    public List<TelemetryDTO> getTelemetryHistory(Long vehicleId, int hoursBack) {
        LocalDateTime since = LocalDateTime.now().minusHours(hoursBack);
        return telemetryRepository.findByVehicleIdAndRecordedAfter(vehicleId, since)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TelemetryDTO recordTelemetry(Long vehicleId, TelemetryDTO dto) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        if (vehicleOpt.isEmpty()) {
            return null;
        }

        Telemetry telemetry = Telemetry.builder()
                .vehicle(vehicleOpt.get())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .speedKmh(dto.getSpeedKmh())
                .batteryPercentage(dto.getBatteryPercentage())
                .fuelPercentage(dto.getFuelPercentage())
                .temperatureCelsius(dto.getTemperatureCelsius())
                .recordedAt(LocalDateTime.now())
                .build();

        Telemetry saved = telemetryRepository.save(telemetry);
        return mapToDTO(saved);
    }

    public TelemetryDTO simulateTelemetry(Long vehicleId) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        if (vehicleOpt.isEmpty()) {
            return null;
        }

        Vehicle vehicle = vehicleOpt.get();

        // Simulate slight variations in location
        BigDecimal newLat = vehicle.getCurrentLatitude() != null ?
                vehicle.getCurrentLatitude().add(BigDecimal.valueOf((random.nextDouble() - 0.5) * 0.01)) :
                BigDecimal.valueOf(28.6139);
        BigDecimal newLong = vehicle.getCurrentLongitude() != null ?
                vehicle.getCurrentLongitude().add(BigDecimal.valueOf((random.nextDouble() - 0.5) * 0.01)) :
                BigDecimal.valueOf(77.2090);

        int speed = "IN_USE".equals(vehicle.getStatus()) ? random.nextInt(80) + 20 : 0;
        int battery = vehicle.getBatteryPercentage() != null ?
                Math.max(10, vehicle.getBatteryPercentage() - random.nextInt(3)) : 85;
        int fuel = vehicle.getFuelPercentage() != null ?
                Math.max(10, vehicle.getFuelPercentage() - random.nextInt(2)) : 85;

        Telemetry telemetry = Telemetry.builder()
                .vehicle(vehicle)
                .latitude(newLat)
                .longitude(newLong)
                .speedKmh(speed)
                .batteryPercentage(battery)
                .fuelPercentage(fuel)
                .temperatureCelsius(20 + random.nextInt(15))
                .recordedAt(LocalDateTime.now())
                .build();

        Telemetry saved = telemetryRepository.save(telemetry);

        // Update vehicle with latest telemetry
        vehicle.setCurrentLatitude(newLat);
        vehicle.setCurrentLongitude(newLong);
        vehicle.setSpeedKmh(speed);
        vehicle.setBatteryPercentage(battery);
        vehicle.setFuelPercentage(fuel);
        vehicleRepository.save(vehicle);

        return mapToDTO(saved);
    }

    private TelemetryDTO mapToDTO(Telemetry telemetry) {
        return TelemetryDTO.builder()
                .id(telemetry.getId())
                .vehicleId(telemetry.getVehicle().getId())
                .latitude(telemetry.getLatitude())
                .longitude(telemetry.getLongitude())
                .speedKmh(telemetry.getSpeedKmh())
                .batteryPercentage(telemetry.getBatteryPercentage())
                .fuelPercentage(telemetry.getFuelPercentage())
                .temperatureCelsius(telemetry.getTemperatureCelsius())
                .recordedAt(telemetry.getRecordedAt())
                .build();
    }
}
