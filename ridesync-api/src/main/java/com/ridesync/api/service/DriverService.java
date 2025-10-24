package com.ridesync.api.service;

import com.ridesync.api.dto.RegisterDriverRequest;
import com.ridesync.core.model.Driver;
import com.ridesync.core.model.Location;
import com.ridesync.persistence.mapper.DriverMapper;
import com.ridesync.persistence.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for driver management operations
 */
@Service
@RequiredArgsConstructor
public class DriverService {
    
    private final DriverRepository driverRepository;
    
    /**
     * Register a new driver
     */
    @Transactional
    public Driver registerDriver(RegisterDriverRequest request) {
        Driver driver = Driver.builder()
            .name(request.getName())
            .phoneNumber(request.getPhoneNumber())
            .vehicleType(request.getVehicleType())
            .licensePlate(request.getLicensePlate())
            .currentLocation(new Location(request.getCurrentLatitude(), request.getCurrentLongitude()))
            .build();
        
        var saved = driverRepository.save(DriverMapper.toEntity(driver));
        return DriverMapper.toDomain(saved);
    }
    
    /**
     * Get driver by ID
     */
    public Driver getDriver(String id) {
        return driverRepository.findById(id)
            .map(DriverMapper::toDomain)
            .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + id));
    }
    
    /**
     * Get all drivers
     */
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll()
            .stream()
            .map(DriverMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    /**
     * Get available drivers
     */
    public List<Driver> getAvailableDrivers() {
        return driverRepository.findAvailableDrivers()
            .stream()
            .map(DriverMapper::toDomain)
            .collect(Collectors.toList());
    }
}
