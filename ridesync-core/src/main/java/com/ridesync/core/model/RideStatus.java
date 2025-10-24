package com.ridesync.core.model;

/**
 * Enumeration of possible ride statuses throughout its lifecycle.
 */
public enum RideStatus {
    REQUESTED,      // Ride has been requested but no driver assigned
    ASSIGNED,       // Driver has been assigned
    STARTED,        // Ride is in progress
    COMPLETED,      // Ride has been completed successfully
    CANCELLED       // Ride was cancelled
}
