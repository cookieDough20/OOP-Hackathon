package com.ridesync.core.exception;

/**
 * Custom exception thrown when no driver is available for ride allocation
 * Demonstrates custom exception handling as per requirements
 */
public class NoDriverAvailableException extends RuntimeException {
    
    private final String riderId;
    private final String location;
    
    public NoDriverAvailableException(String riderId, String location) {
        super(String.format("No drivers available for rider %s at location %s", riderId, location));
        this.riderId = riderId;
        this.location = location;
    }
    
    public NoDriverAvailableException(String message) {
        super(message);
        this.riderId = null;
        this.location = null;
    }
    
    public String getRiderId() {
        return riderId;
    }
    
    public String getLocation() {
        return location;
    }
}
