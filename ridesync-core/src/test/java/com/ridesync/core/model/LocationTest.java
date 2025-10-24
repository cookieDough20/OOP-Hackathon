package com.ridesync.core.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Location class and Haversine distance calculation.
 */
class LocationTest {
    
    @Test
    void testDistanceCalculation() {
        // Bangalore MG Road to Whitefield
        Location location1 = Location.builder()
                .latitude(12.9716)
                .longitude(77.5946)
                .build();
        
        Location location2 = Location.builder()
                .latitude(12.9698)
                .longitude(77.7500)
                .build();
        
        double distance = location1.distanceTo(location2);
        
        // Distance should be approximately 13-15 km
        assertTrue(distance > 12 && distance < 16, 
            "Distance should be between 12 and 16 km, got: " + distance);
    }
    
    @Test
    void testZeroDistance() {
        Location location = Location.builder()
                .latitude(12.9716)
                .longitude(77.5946)
                .build();
        
        double distance = location.distanceTo(location);
        assertEquals(0.0, distance, 0.01, "Distance to same location should be 0");
    }
}
