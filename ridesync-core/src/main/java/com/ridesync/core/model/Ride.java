package com.ridesync.core.model;

import com.ridesync.core.strategy.FareStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Abstract base class for all ride types in the system.
 * Implements the Template Method pattern for fare calculation.
 * Uses sealed class concept (via inheritance) for type safety.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Ride {
    private String id;
    private String riderId;
    private String driverId;
    private Location startLocation;
    private Location endLocation;
    private RideType rideType;
    private RideStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private double distance; // in kilometers
    private double fare;
    private double surgeMultiplier;
    
    /**
     * Calculate fare using the Strategy pattern.
     * This is the template method that delegates to the strategy.
     * 
     * @param fareStrategy The fare calculation strategy to use
     * @param distance Distance of the ride in kilometers
     * @param surgeMultiplier Current surge pricing multiplier
     * @return Calculated fare amount
     */
    public abstract double calculateFare(FareStrategy fareStrategy, double distance, double surgeMultiplier);
    
    /**
     * Get the base fare rate for this ride type.
     * Subclasses override this to provide their specific rates.
     */
    public abstract double getBaseFareRate();
}
