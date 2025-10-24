package com.ridesync.core.model.enums;

/**
 * Enum for vehicle classifications
 */
public enum VehicleType {
    SEDAN("4-door sedan"),
    SUV("Sport Utility Vehicle"),
    LUXURY("Luxury/Premium vehicle"),
    MINI("Compact car");
    
    private final String description;
    
    VehicleType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
