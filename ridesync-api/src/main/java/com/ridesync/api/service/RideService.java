package com.ridesync.api.service;

import com.ridesync.api.dto.BookRideRequest;
import com.ridesync.api.dto.RideResponse;
import com.ridesync.core.exception.InvalidRideRequestException;
import com.ridesync.core.exception.NoDriverAvailableException;
import com.ridesync.core.exception.RideNotFoundException;
import com.ridesync.core.factory.RideFactory;
import com.ridesync.core.model.*;
import com.ridesync.core.service.RideAllocator;
import com.ridesync.core.service.RideLogger;
import com.ridesync.persistence.entity.DriverEntity;
import com.ridesync.persistence.entity.RideEntity;
import com.ridesync.persistence.mapper.EntityMapper;
import com.ridesync.persistence.repository.DriverRepository;
import com.ridesync.persistence.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for ride management operations.
 * Orchestrates ride booking, tracking, and completion.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RideService {
    
    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final RideAllocator rideAllocator = RideAllocator.getInstance();
    private final RideLogger rideLogger = new RideLogger();
    private final SimpMessagingTemplate messagingTemplate;
    
    /**
     * Book a new ride with automatic driver assignment.
     */
    @Transactional
    public RideResponse bookRide(BookRideRequest request) {
        log.info("Booking ride for rider: {}", request.getRiderId());
        
        // Validate request
        validateBookingRequest(request);
        
        // Create ride using Factory pattern
        Location startLocation = Location.builder()
                .latitude(request.getStartLatitude())
                .longitude(request.getStartLongitude())
                .address(request.getStartAddress())
                .build();
        
        Location endLocation = Location.builder()
                .latitude(request.getEndLatitude())
                .longitude(request.getEndLongitude())
                .address(request.getEndAddress())
                .build();
        
        Ride ride = RideFactory.createRide(
            request.getRideType(),
            request.getRiderId(),
            startLocation,
            endLocation
        );
        
        // Get available drivers
        List<DriverEntity> driverEntities = driverRepository.findByStatus(DriverStatus.AVAILABLE);
        List<Driver> availableDrivers = driverEntities.stream()
                .map(EntityMapper::toDomain)
                .collect(Collectors.toList());
        
        // Assign driver using singleton allocator
        Driver assignedDriver;
        try {
            assignedDriver = rideAllocator.assignDriver(ride, availableDrivers);
        } catch (NoDriverAvailableException e) {
            log.error("No driver available", e);
            throw e;
        }
        
        // Update driver in database
        DriverEntity driverEntity = EntityMapper.toEntity(assignedDriver);
        driverRepository.save(driverEntity);
        
        // Save ride to database
        RideEntity rideEntity = EntityMapper.toEntity(ride);
        rideRepository.save(rideEntity);
        
        // Log to file
        rideLogger.logRide(ride);
        
        // Send WebSocket notification
        sendRideUpdate(ride, "Driver assigned: " + assignedDriver.getName());
        
        log.info("Ride {} booked successfully with driver {}", ride.getId(), assignedDriver.getId());
        
        return RideResponse.builder()
                .rideId(ride.getId())
                .riderId(ride.getRiderId())
                .driverId(assignedDriver.getId())
                .driverName(assignedDriver.getName())
                .driverVehicle(assignedDriver.getVehicle())
                .rideType(ride.getRideType())
                .status(ride.getStatus())
                .distance(ride.getDistance())
                .estimatedFare(ride.getFare())
                .surgeMultiplier(ride.getSurgeMultiplier())
                .requestedAt(ride.getRequestedAt())
                .message("Ride booked successfully! Your driver will arrive soon.")
                .build();
    }
    
    /**
     * Get ride details by ID.
     */
    public RideResponse getRide(String rideId) {
        RideEntity rideEntity = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException(rideId));
        
        DriverEntity driverEntity = rideEntity.getDriverId() != null ? 
                driverRepository.findById(rideEntity.getDriverId()).orElse(null) : null;
        
        return RideResponse.builder()
                .rideId(rideEntity.getId())
                .riderId(rideEntity.getRiderId())
                .driverId(rideEntity.getDriverId())
                .driverName(driverEntity != null ? driverEntity.getName() : null)
                .driverVehicle(driverEntity != null ? driverEntity.getVehicle() : null)
                .rideType(rideEntity.getRideType())
                .status(rideEntity.getStatus())
                .distance(rideEntity.getDistance())
                .actualFare(rideEntity.getFare())
                .surgeMultiplier(rideEntity.getSurgeMultiplier())
                .requestedAt(rideEntity.getRequestedAt())
                .startedAt(rideEntity.getStartedAt())
                .completedAt(rideEntity.getCompletedAt())
                .build();
    }
    
    /**
     * Start a ride.
     */
    @Transactional
    public RideResponse startRide(String rideId) {
        RideEntity rideEntity = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException(rideId));
        
        // Convert to domain model
        Ride ride = convertToDomainRide(rideEntity);
        
        rideAllocator.startRide(ride);
        
        // Update database
        rideEntity.setStatus(ride.getStatus());
        rideEntity.setStartedAt(ride.getStartedAt());
        rideRepository.save(rideEntity);
        
        // Log update
        rideLogger.logRide(ride);
        
        // WebSocket notification
        sendRideUpdate(ride, "Ride started");
        
        return getRide(rideId);
    }
    
    /**
     * Complete a ride and calculate earnings.
     */
    @Transactional
    public RideResponse completeRide(String rideId) {
        RideEntity rideEntity = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException(rideId));
        
        DriverEntity driverEntity = driverRepository.findById(rideEntity.getDriverId())
                .orElseThrow(() -> new IllegalStateException("Driver not found"));
        
        // Convert to domain models
        Ride ride = convertToDomainRide(rideEntity);
        Driver driver = EntityMapper.toDomain(driverEntity);
        
        // Complete ride using allocator
        rideAllocator.completeRide(ride, driver);
        
        // Update database
        rideEntity.setStatus(ride.getStatus());
        rideEntity.setCompletedAt(ride.getCompletedAt());
        rideRepository.save(rideEntity);
        
        driverEntity.setStatus(driver.getStatus());
        driverEntity.setTotalEarnings(driver.getTotalEarnings());
        driverRepository.save(driverEntity);
        
        // Log completion
        rideLogger.logRide(ride);
        
        // WebSocket notification
        sendRideUpdate(ride, "Ride completed! Fare: ₹" + ride.getFare());
        
        return getRide(rideId);
    }
    
    /**
     * Get all rides for a rider.
     */
    public List<RideResponse> getRiderRides(String riderId) {
        return rideRepository.findByRiderId(riderId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all rides for a driver.
     */
    public List<RideResponse> getDriverRides(String driverId) {
        return rideRepository.findByDriverId(driverId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // Helper methods
    
    private void validateBookingRequest(BookRideRequest request) {
        double distance = new Location(request.getStartLatitude(), request.getStartLongitude(), "")
                .distanceTo(new Location(request.getEndLatitude(), request.getEndLongitude(), ""));
        
        if (distance < 0.5) {
            throw new InvalidRideRequestException("Ride distance must be at least 0.5 km");
        }
        
        if (distance > 500) {
            throw new InvalidRideRequestException("Ride distance cannot exceed 500 km");
        }
    }
    
    private Ride convertToDomainRide(RideEntity entity) {
        Location start = Location.builder()
                .latitude(entity.getStartLatitude())
                .longitude(entity.getStartLongitude())
                .address(entity.getStartAddress())
                .build();
        
        Location end = Location.builder()
                .latitude(entity.getEndLatitude())
                .longitude(entity.getEndLongitude())
                .address(entity.getEndAddress())
                .build();
        
        return RideFactory.createRideWithSurge(
            entity.getRideType(),
            entity.getRiderId(),
            start,
            end,
            entity.getSurgeMultiplier()
        );
    }
    
    private RideResponse convertToResponse(RideEntity entity) {
        return RideResponse.builder()
                .rideId(entity.getId())
                .riderId(entity.getRiderId())
                .driverId(entity.getDriverId())
                .rideType(entity.getRideType())
                .status(entity.getStatus())
                .distance(entity.getDistance())
                .actualFare(entity.getFare())
                .surgeMultiplier(entity.getSurgeMultiplier())
                .requestedAt(entity.getRequestedAt())
                .startedAt(entity.getStartedAt())
                .completedAt(entity.getCompletedAt())
                .build();
    }
    
    private void sendRideUpdate(Ride ride, String message) {
        try {
            RideResponse response = RideResponse.builder()
                    .rideId(ride.getId())
                    .status(ride.getStatus())
                    .message(message)
                    .build();
            
            messagingTemplate.convertAndSend("/topic/rides/" + ride.getRiderId(), response);
        } catch (Exception e) {
            log.error("Failed to send WebSocket update", e);
        }
    }
}
