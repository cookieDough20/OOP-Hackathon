package com.ridesync.core.service;

import com.ridesync.core.exception.NoDriverAvailableException;
import com.ridesync.core.model.Driver;
import com.ridesync.core.model.Location;
import com.ridesync.core.model.Ride;
import com.ridesync.core.model.enums.VehicleType;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Singleton service for thread-safe ride allocation
 * Demonstrates Singleton Pattern and thread-safety with synchronized methods
 * Uses ReentrantLock for fine-grained concurrency control
 */
public class RideAllocator {
    
    private static volatile RideAllocator instance;
    private final ReentrantLock allocationLock = new ReentrantLock();
    
    // Private constructor for singleton
    private RideAllocator() {
    }
    
    /**
     * Double-checked locking singleton pattern
     * Thread-safe lazy initialization
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
     * Allocate a driver to a ride using thread-safe mechanism
     * Prevents race conditions during concurrent bookings
     * 
     * @param ride The ride to allocate a driver for
     * @param availableDrivers List of available drivers
     * @return Assigned driver
     * @throws NoDriverAvailableException if no suitable driver found
     */
    public Driver allocateDriver(Ride ride, List<Driver> availableDrivers) 
            throws NoDriverAvailableException {
        
        allocationLock.lock();
        try {
            // Find nearest available driver using Java Streams
            Optional<Driver> nearestDriver = availableDrivers.stream()
                .filter(Driver::isAvailable)
                .filter(driver -> isDriverSuitableForRide(driver, ride))
                .min(Comparator.comparingDouble(driver -> 
                    calculateDistance(driver.getCurrentLocation(), ride.getStartLocation())
                ));
            
            if (nearestDriver.isEmpty()) {
                throw new NoDriverAvailableException(
                    ride.getRiderId(), 
                    String.format("%.2f,%.2f", 
                        ride.getStartLocation().latitude(), 
                        ride.getStartLocation().longitude())
                );
            }
            
            Driver driver = nearestDriver.get();
            driver.assignToRide();
            ride.assignDriver(driver.getId());
            
            return driver;
            
        } finally {
            allocationLock.unlock();
        }
    }
    
    /**
     * Check if driver is suitable for the ride type
     */
    private boolean isDriverSuitableForRide(Driver driver, Ride ride) {
        // Luxury rides require luxury vehicles
        if (ride.getRideType().name().equals("LUXURY")) {
            return driver.getVehicleType() == VehicleType.LUXURY || 
                   driver.getVehicleType() == VehicleType.SUV;
        }
        return true;
    }
    
    /**
     * Calculate distance between two locations
     */
    private double calculateDistance(Location from, Location to) {
        return from.distanceTo(to);
    }
    
    /**
     * Simulate concurrent allocation for testing
     * Demonstrates thread-safety under concurrent load
     */
    public synchronized void simulateConcurrentAllocation(List<Ride> rides, 
                                                         List<Driver> drivers) {
        rides.parallelStream().forEach(ride -> {
            try {
                allocateDriver(ride, drivers);
            } catch (NoDriverAvailableException e) {
                // Log and continue
                System.err.println("Allocation failed: " + e.getMessage());
            }
        });
    }
}
