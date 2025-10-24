package com.ridesync.core.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ridesync.core.model.enums.RideStatus;
import com.ridesync.core.model.enums.RideType;
import com.ridesync.core.strategy.FareStrategy;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Abstract base class for all ride types
 * Demonstrates OOP principles: abstraction, inheritance, and polymorphism
 * Uses Jackson annotations for proper JSON serialization of inheritance hierarchy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = StandardRide.class, name = "STANDARD"),
    @JsonSubTypes.Type(value = PoolRide.class, name = "POOL"),
    @JsonSubTypes.Type(value = LuxuryRide.class, name = "LUXURY")
})
public abstract class Ride {
    
    private String id = UUID.randomUUID().toString();
    
    @NotNull(message = "Rider ID is required")
    private String riderId;
    
    private String driverId;
    
    @NotNull(message = "Start location is required")
    private Location startLocation;
    
    @NotNull(message = "End location is required")
    private Location endLocation;
    
    private double distance; // in kilometers
    
    private double fare;
    
    private RideStatus status = RideStatus.REQUESTED;
    
    private LocalDateTime requestedAt = LocalDateTime.now();
    
    private LocalDateTime startedAt;
    
    private LocalDateTime completedAt;
    
    /**
     * Get the type of this ride
     */
    public abstract RideType getRideType();
    
    /**
     * Calculate fare using the provided strategy pattern
     * Demonstrates Strategy Pattern for flexible fare calculation
     * 
     * @param strategy The fare calculation strategy to use
     * @param surgeMultiplier Dynamic surge pricing multiplier
     * @return Calculated fare amount
     */
    public double calculateFare(FareStrategy strategy, double surgeMultiplier) {
        this.distance = startLocation.distanceTo(endLocation);
        this.fare = strategy.calculateFare(this, distance, surgeMultiplier);
        return this.fare;
    }
    
    /**
     * Assign a driver to this ride
     */
    public void assignDriver(String driverId) {
        this.driverId = driverId;
        this.status = RideStatus.DRIVER_ASSIGNED;
    }
    
    /**
     * Start the ride
     */
    public void startRide() {
        this.status = RideStatus.IN_PROGRESS;
        this.startedAt = LocalDateTime.now();
    }
    
    /**
     * Complete the ride
     */
    public void completeRide() {
        this.status = RideStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
    
    /**
     * Cancel the ride
     */
    public void cancelRide() {
        this.status = RideStatus.CANCELLED;
    }
}
