package com.ridesync.api.service;

import com.ridesync.api.dto.DriverRequest;
import com.ridesync.core.model.Driver;
import com.ridesync.core.model.DriverStatus;
import com.ridesync.core.model.Location;
import com.ridesync.persistence.entity.DriverEntity;
import com.ridesync.persistence.mapper.EntityMapper;
import com.ridesync.persistence.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for driver management operations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DriverService {
    
    private final DriverRepository driverRepository;
    
    /**
     * Register a new driver.
     */
    @Transactional
    public Driver registerDriver(DriverRequest request) {
        String driverId = "DRV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Driver driver = Driver.builder()
                .id(driverId)
                .name(request.getName())
                .vehicle(request.getVehicle())
                .vehicleNumber(request.getVehicleNumber())
                .status(DriverStatus.AVAILABLE)
                .currentLocation(Location.builder()
                    .latitude(request.getLatitude())
                    .longitude(request.getLongitude())
                    .address(request.getAddress())
                    .build())
                .totalEarnings(0.0)
                .rating(5.0)
                .build();
        
        DriverEntity entity = EntityMapper.toEntity(driver);
        driverRepository.save(entity);
        
        log.info("Registered new driver: {} with ID: {}", driver.getName(), driver.getId());
        return driver;
    }
    
    /**
     * Get driver by ID.
     */
    public Driver getDriver(String driverId) {
        DriverEntity entity = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found: " + driverId));
        return EntityMapper.toDomain(entity);
    }
    
    /**
     * Get all drivers.
     */
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll().stream()
                .map(EntityMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Get available drivers.
     */
    public List<Driver> getAvailableDrivers() {
        return driverRepository.findByStatus(DriverStatus.AVAILABLE).stream()
                .map(EntityMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Update driver location.
     */
    @Transactional
    public void updateDriverLocation(String driverId, double latitude, double longitude, String address) {
        DriverEntity entity = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found: " + driverId));
        
        entity.setCurrentLatitude(latitude);
        entity.setCurrentLongitude(longitude);
        entity.setCurrentAddress(address);
        
        driverRepository.save(entity);
        log.info("Updated location for driver: {}", driverId);
    }
}
