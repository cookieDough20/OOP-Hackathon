package com.ridesync.core.model.enums;

/**
 * Enum for different types of rides offered
 */
public enum RideType {
    STANDARD("Standard ride for solo passengers"),
    POOL("Shared ride for cost-conscious passengers"),
    LUXURY("Premium ride with luxury vehicles");
    
    private final String description;
    
    RideType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
