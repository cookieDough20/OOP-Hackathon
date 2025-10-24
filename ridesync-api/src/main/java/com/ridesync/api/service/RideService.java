package com.ridesync.api.service;

import com.ridesync.api.dto.BookRideRequest;
import com.ridesync.api.dto.RideResponse;
import com.ridesync.core.exception.InsufficientBalanceException;
import com.ridesync.core.exception.NoDriverAvailableException;
import com.ridesync.core.exception.RideNotFoundException;
import com.ridesync.core.factory.FareStrategyFactory;
import com.ridesync.core.factory.RideFactory;
import com.ridesync.core.model.Driver;
import com.ridesync.core.model.Location;
import com.ridesync.core.model.Ride;
import com.ridesync.core.model.Rider;
import com.ridesync.core.model.enums.RideStatus;
import com.ridesync.core.service.RideAllocator;
import com.ridesync.core.service.SurgePricingService;
import com.ridesync.core.strategy.FareStrategy;
import com.ridesync.persistence.mapper.DriverMapper;
import com.ridesync.persistence.mapper.RideMapper;
import com.ridesync.persistence.mapper.RiderMapper;
import com.ridesync.persistence.repository.DriverRepository;
import com.ridesync.persistence.repository.RideRepository;
import com.ridesync.persistence.repository.RiderRepository;
import com.ridesync.persistence.service.RideLoggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main service for ride booking and management
 * Demonstrates business logic orchestration
 */
@Service
@RequiredArgsConstructor
public class RideService {
    
    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;
    private final RideLoggerService rideLoggerService;
    private final SurgePricingService surgePricingService = new SurgePricingService();
    private final RideAllocator rideAllocator = RideAllocator.getInstance();
    private final SimpMessagingTemplate messagingTemplate;
    
    // In-memory storage for ride history (as per requirements)
    private final Map<String, List<Ride>> driverRideHistory = new HashMap<>();
    
    /**
     * Book a new ride with all business logic
     */
    @Transactional
    public RideResponse bookRide(BookRideRequest request) {
        // Validate rider exists
        var riderEntity = riderRepository.findById(request.getRiderId())
            .orElseThrow(() -> new RuntimeException("Rider not found"));
        Rider rider = RiderMapper.toDomain(riderEntity);
        
        // Create ride using Factory Pattern
        Location start = new Location(request.getStartLatitude(), request.getStartLongitude());
        Location end = new Location(request.getEndLatitude(), request.getEndLongitude());
        Ride ride = RideFactory.createRide(request.getRideType(), rider.getId(), start, end);
        
        // Calculate surge pricing
        int activeDemand = (int) rideRepository.countActiveRides();
        double surgeMultiplier = surgePricingService.getCurrentSurgeMultiplier(start, activeDemand);
        
        // Calculate fare using Strategy Pattern
        FareStrategy fareStrategy = FareStrategyFactory.getStrategy(request.getRideType());
        double estimatedFare = ride.calculateFare(fareStrategy, surgeMultiplier);
        
        // Check rider balance
        if (!rider.hasSufficientBalance(estimatedFare)) {
            throw new InsufficientBalanceException(
                rider.getId(), 
                estimatedFare, 
                rider.getWalletBalance()
            );
        }
        
        // Allocate driver using thread-safe Singleton
        List<Driver> availableDrivers = driverRepository.findAvailableDrivers()
            .stream()
            .map(DriverMapper::toDomain)
            .collect(Collectors.toList());
        
        Driver assignedDriver = rideAllocator.allocateDriver(ride, availableDrivers);
        
        // Persist to database
        var savedRide = rideRepository.save(RideMapper.toEntity(ride));
        var updatedDriver = driverRepository.save(DriverMapper.toEntity(assignedDriver));
        
        // Log to file (File I/O demonstration)
        rideLoggerService.logRide(ride);
        
        // Add to in-memory ride history
        driverRideHistory.computeIfAbsent(assignedDriver.getId(), k -> new java.util.ArrayList<>())
            .add(ride);
        
        // Send WebSocket notification
        sendRideUpdateNotification(ride, assignedDriver);
        
        return buildRideResponse(ride, assignedDriver, surgeMultiplier, 
            "Ride booked successfully! Driver is on the way.");
    }
    
    /**
     * Get ride details by ID
     */
    public RideResponse getRide(String rideId) {
        var rideEntity = rideRepository.findById(rideId)
            .orElseThrow(() -> new RideNotFoundException(rideId));
        
        Ride ride = RideMapper.toDomain(rideEntity);
        Driver driver = null;
        
        if (ride.getDriverId() != null) {
            var driverEntity = driverRepository.findById(ride.getDriverId()).orElse(null);
            driver = driverEntity != null ? DriverMapper.toDomain(driverEntity) : null;
        }
        
        return buildRideResponse(ride, driver, 1.0, "Ride details retrieved");
    }
    
    /**
     * Complete a ride
     */
    @Transactional
    public RideResponse completeRide(String rideId) {
        var rideEntity = rideRepository.findById(rideId)
            .orElseThrow(() -> new RideNotFoundException(rideId));
        
        Ride ride = RideMapper.toDomain(rideEntity);
        ride.completeRide();
        
        // Update driver
        var driverEntity = driverRepository.findById(ride.getDriverId())
            .orElseThrow(() -> new RuntimeException("Driver not found"));
        Driver driver = DriverMapper.toDomain(driverEntity);
        driver.completeRide(ride);
        
        // Update rider
        var riderEntity = riderRepository.findById(ride.getRiderId())
            .orElseThrow(() -> new RuntimeException("Rider not found"));
        Rider rider = RiderMapper.toDomain(riderEntity);
        rider.deductFare(ride.getFare());
        rider.addRide(ride);
        
        // Save updates
        rideRepository.save(RideMapper.toEntity(ride));
        driverRepository.save(DriverMapper.toEntity(driver));
        riderRepository.save(RiderMapper.toEntity(rider));
        
        // Log completion
        rideLoggerService.logRide(ride);
        
        // Send WebSocket notification
        sendRideUpdateNotification(ride, driver);
        
        return buildRideResponse(ride, driver, 1.0, 
            String.format("Ride completed! Fare: %.2f. Thank you for using RideSync!", ride.getFare()));
    }
    
    /**
     * Start a ride
     */
    @Transactional
    public RideResponse startRide(String rideId) {
        var rideEntity = rideRepository.findById(rideId)
            .orElseThrow(() -> new RideNotFoundException(rideId));
        
        Ride ride = RideMapper.toDomain(rideEntity);
        ride.startRide();
        
        rideRepository.save(RideMapper.toEntity(ride));
        
        var driver = driverRepository.findById(ride.getDriverId())
            .map(DriverMapper::toDomain)
            .orElse(null);
        
        sendRideUpdateNotification(ride, driver);
        
        return buildRideResponse(ride, driver, 1.0, "Ride started!");
    }
    
    /**
     * Cancel a ride
     */
    @Transactional
    public RideResponse cancelRide(String rideId) {
        var rideEntity = rideRepository.findById(rideId)
            .orElseThrow(() -> new RideNotFoundException(rideId));
        
        Ride ride = RideMapper.toDomain(rideEntity);
        ride.cancelRide();
        
        // Free up driver
        if (ride.getDriverId() != null) {
            var driverEntity = driverRepository.findById(ride.getDriverId()).orElse(null);
            if (driverEntity != null) {
                Driver driver = DriverMapper.toDomain(driverEntity);
                driver.setStatus(com.ridesync.core.model.enums.DriverStatus.AVAILABLE);
                driverRepository.save(DriverMapper.toEntity(driver));
            }
        }
        
        rideRepository.save(RideMapper.toEntity(ride));
        
        return RideResponse.builder()
            .rideId(ride.getId())
            .status(RideStatus.CANCELLED)
            .message("Ride cancelled successfully")
            .build();
    }
    
    /**
     * Send WebSocket notification for ride updates
     */
    private void sendRideUpdateNotification(Ride ride, Driver driver) {
        try {
            Map<String, Object> notification = new HashMap<>();
            notification.put("rideId", ride.getId());
            notification.put("status", ride.getStatus());
            notification.put("driverId", driver != null ? driver.getId() : null);
            notification.put("driverName", driver != null ? driver.getName() : null);
            notification.put("timestamp", LocalDateTime.now());
            
            messagingTemplate.convertAndSend("/topic/rides/" + ride.getRiderId(), notification);
            if (driver != null) {
                messagingTemplate.convertAndSend("/topic/rides/" + driver.getId(), notification);
            }
        } catch (Exception e) {
            // Log but don't fail the operation
            System.err.println("Failed to send WebSocket notification: " + e.getMessage());
        }
    }
    
    /**
     * Build ride response DTO
     */
    private RideResponse buildRideResponse(Ride ride, Driver driver, double surgeMultiplier, String message) {
        return RideResponse.builder()
            .rideId(ride.getId())
            .riderId(ride.getRiderId())
            .driverId(driver != null ? driver.getId() : null)
            .driverName(driver != null ? driver.getName() : null)
            .driverPhone(driver != null ? driver.getPhoneNumber() : null)
            .vehicleInfo(driver != null ? 
                String.format("%s - %s", driver.getVehicleType(), driver.getLicensePlate()) : null)
            .rideType(ride.getRideType())
            .status(ride.getStatus())
            .distance(ride.getDistance())
            .estimatedFare(ride.getFare())
            .actualFare(ride.getStatus() == RideStatus.COMPLETED ? ride.getFare() : 0)
            .surgeMultiplier(surgeMultiplier)
            .requestedAt(ride.getRequestedAt())
            .estimatedArrival(ride.getRequestedAt().plusMinutes((long)(ride.getDistance() * 2)))
            .message(message)
            .build();
    }
}
