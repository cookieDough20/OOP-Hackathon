package com.ridesync.core.exception;

/**
 * Exception thrown when a ride is not found
 */
public class RideNotFoundException extends RuntimeException {
    
    private final String rideId;
    
    public RideNotFoundException(String rideId) {
        super(String.format("Ride not found with ID: %s", rideId));
        this.rideId = rideId;
    }
    
    public String getRideId() {
        return rideId;
    }
}
