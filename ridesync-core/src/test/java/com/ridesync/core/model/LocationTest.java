package com.ridesync.core.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for Location record
 */
@DisplayName("Location Tests")
class LocationTest {
    
    @Test
    @DisplayName("Should calculate distance between two locations correctly")
    void shouldCalculateDistance() {
        // NYC coordinates
        Location loc1 = new Location(40.7128, -74.0060);
        Location loc2 = new Location(40.7580, -73.9855);
        
        double distance = loc1.distanceTo(loc2);
        
        // Distance should be approximately 5-6 km
        assertThat(distance).isBetween(5.0, 6.0);
    }
    
    @Test
    @DisplayName("Should return zero distance for same location")
    void shouldReturnZeroForSameLocation() {
        Location loc = new Location(40.7128, -74.0060);
        
        double distance = loc.distanceTo(loc);
        
        assertThat(distance).isEqualTo(0.0);
    }
    
    @Test
    @DisplayName("Should create location with valid coordinates")
    void shouldCreateLocationWithValidCoordinates() {
        Location loc = new Location(40.7128, -74.0060);
        
        assertThat(loc.latitude()).isEqualTo(40.7128);
        assertThat(loc.longitude()).isEqualTo(-74.0060);
    }
}
