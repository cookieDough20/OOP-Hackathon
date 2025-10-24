package com.ridesync.core.model.enums;

/**
 * Enum representing the lifecycle status of a ride
 */
public enum RideStatus {
    REQUESTED("Ride has been requested"),
    DRIVER_ASSIGNED("Driver has been assigned to the ride"),
    IN_PROGRESS("Ride is currently in progress"),
    COMPLETED("Ride has been completed successfully"),
    CANCELLED("Ride was cancelled");
    
    private final String description;
    
    RideStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
