package com.ridesync.core.exception;

/**
 * Exception thrown when a requested ride is not found.
 */
public class RideNotFoundException extends RuntimeException {
    
    public RideNotFoundException(String rideId) {
        super("Ride not found with ID: " + rideId);
    }
}
