package com.ridesync.core.model.enums;

/**
 * Enum representing driver availability status
 */
public enum DriverStatus {
    AVAILABLE("Driver is available for new rides"),
    BUSY("Driver is currently on a ride"),
    OFFLINE("Driver is not accepting rides");
    
    private final String description;
    
    DriverStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
