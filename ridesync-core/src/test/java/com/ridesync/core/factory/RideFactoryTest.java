package com.ridesync.core.factory;

import com.ridesync.core.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RideFactory pattern implementation.
 */
class RideFactoryTest {
    
    @Test
    void testCreateStandardRide() {
        Location start = Location.builder()
                .latitude(12.9716)
                .longitude(77.5946)
                .build();
        
        Location end = Location.builder()
                .latitude(12.9698)
                .longitude(77.7500)
                .build();
        
        Ride ride = RideFactory.createRide(RideType.STANDARD, "rider123", start, end);
        
        assertNotNull(ride);
        assertTrue(ride instanceof StandardRide);
        assertEquals(RideType.STANDARD, ride.getRideType());
        assertEquals(RideStatus.REQUESTED, ride.getStatus());
        assertEquals("rider123", ride.getRiderId());
        assertTrue(ride.getDistance() > 0);
    }
    
    @Test
    void testCreatePoolRide() {
        Location start = Location.builder().latitude(12.9716).longitude(77.5946).build();
        Location end = Location.builder().latitude(12.9698).longitude(77.7500).build();
        
        Ride ride = RideFactory.createRide(RideType.POOL, "rider456", start, end);
        
        assertNotNull(ride);
        assertTrue(ride instanceof PoolRide);
        assertEquals(RideType.POOL, ride.getRideType());
    }
    
    @Test
    void testCreateLuxuryRide() {
        Location start = Location.builder().latitude(12.9716).longitude(77.5946).build();
        Location end = Location.builder().latitude(12.9698).longitude(77.7500).build();
        
        Ride ride = RideFactory.createRide(RideType.LUXURY, "rider789", start, end);
        
        assertNotNull(ride);
        assertTrue(ride instanceof LuxuryRide);
        assertEquals(RideType.LUXURY, ride.getRideType());
        
        LuxuryRide luxuryRide = (LuxuryRide) ride;
        assertNotNull(luxuryRide.getAmenities());
    }
    
    @Test
    void testCreateRideWithSurge() {
        Location start = Location.builder().latitude(12.9716).longitude(77.5946).build();
        Location end = Location.builder().latitude(12.9698).longitude(77.7500).build();
        double surgeMultiplier = 1.8;
        
        Ride ride = RideFactory.createRideWithSurge(
            RideType.STANDARD, "rider123", start, end, surgeMultiplier
        );
        
        assertEquals(surgeMultiplier, ride.getSurgeMultiplier());
    }
}
