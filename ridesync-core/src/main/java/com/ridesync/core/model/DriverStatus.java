package com.ridesync.core.model;

/**
 * Enumeration of driver availability statuses.
 */
public enum DriverStatus {
    AVAILABLE,      // Driver is available for rides
    BUSY,           // Driver is currently on a ride
    OFFLINE         // Driver is not accepting rides
}
