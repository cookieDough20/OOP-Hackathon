package com.ridesync.core.service;

import com.ridesync.core.exception.NoDriverAvailableException;
import com.ridesync.core.factory.FareStrategyFactory;
import com.ridesync.core.model.*;
import com.ridesync.core.strategy.FareStrategy;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Singleton service for thread-safe ride allocation.
 * Handles concurrent ride bookings without race conditions.
 * Implements Singleton pattern for centralized allocation management.
 */
@Slf4j
public class RideAllocator {
    private static volatile RideAllocator instance;
    private final SurgePricingService surgePricingService;
    
    // Private constructor for singleton
    private RideAllocator() {
        this.surgePricingService = new SurgePricingService();
    }
    
    /**
     * Get singleton instance (thread-safe double-checked locking).
     */
    public static RideAllocator getInstance() {
        if (instance == null) {
            synchronized (RideAllocator.class) {
                if (instance == null) {
                    instance = new RideAllocator();
                }
            }
        }
        return instance;
    }
    
    /**
     * Assign a driver to a ride with thread safety.
     * This method is synchronized to prevent race conditions during concurrent bookings.
     * 
     * @param ride The ride to assign
     * @param availableDrivers List of available drivers
     * @return The assigned driver
     * @throws NoDriverAvailableException if no driver is available
     */
    public synchronized Driver assignDriver(Ride ride, List<Driver> availableDrivers) {
        log.info("Attempting to assign driver for ride: {}", ride.getId());
        
        // Find nearest available driver
        Optional<Driver> nearestDriver = availableDrivers.stream()
                .filter(Driver::isAvailable)
                .min(Comparator.comparingDouble(driver -> 
                    driver.getCurrentLocation().distanceTo(ride.getStartLocation())
                ));
        
        if (nearestDriver.isEmpty()) {
            log.error("No driver available for ride: {}", ride.getId());
            throw new NoDriverAvailableException(
                "No drivers available in your area. Please try again later."
            );
        }
        
        Driver driver = nearestDriver.get();
        
        // Update ride with driver assignment
        ride.setDriverId(driver.getId());
        ride.setStatus(RideStatus.ASSIGNED);
        
        // Update driver status
        driver.setStatus(DriverStatus.BUSY);
        
        // Calculate surge and fare
        double surgeMultiplier = surgePricingService.calculateSurgeMultiplier(ride.getStartLocation());
        ride.setSurgeMultiplier(surgeMultiplier);
        
        FareStrategy fareStrategy = FareStrategyFactory.getStrategy(ride.getRideType());
        double fare = ride.calculateFare(fareStrategy, ride.getDistance(), surgeMultiplier);
        
        log.info("Assigned driver {} to ride {} with fare: {}", 
                driver.getId(), ride.getId(), fare);
        
        return driver;
    }
    
    /**
     * Complete a ride and update driver earnings.
     * Synchronized to ensure thread-safe updates.
     */
    public synchronized void completeRide(Ride ride, Driver driver) {
        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());
        
        driver.addCompletedRide(ride);
        driver.setStatus(DriverStatus.AVAILABLE);
        
        log.info("Completed ride {} with earnings: {}", ride.getId(), ride.getFare());
    }
    
    /**
     * Start a ride.
     */
    public synchronized void startRide(Ride ride) {
        ride.setStatus(RideStatus.STARTED);
        ride.setStartedAt(LocalDateTime.now());
        log.info("Started ride: {}", ride.getId());
    }
    
    /**
     * Cancel a ride and free up the driver.
     */
    public synchronized void cancelRide(Ride ride, Driver driver) {
        ride.setStatus(RideStatus.CANCELLED);
        if (driver != null) {
            driver.setStatus(DriverStatus.AVAILABLE);
        }
        log.info("Cancelled ride: {}", ride.getId());
    }
}
