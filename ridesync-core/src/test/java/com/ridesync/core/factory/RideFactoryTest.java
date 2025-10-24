package com.ridesync.core.factory;

import com.ridesync.core.model.*;
import com.ridesync.core.model.enums.RideType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Ride Factory Tests")
class RideFactoryTest {
    
    @Test
    @DisplayName("Should create standard ride")
    void shouldCreateStandardRide() {
        Location start = new Location(40.7128, -74.0060);
        Location end = new Location(40.7580, -73.9855);
        
        Ride ride = RideFactory.createRide(RideType.STANDARD, "rider1", start, end);
        
        assertThat(ride).isInstanceOf(StandardRide.class);
        assertThat(ride.getRideType()).isEqualTo(RideType.STANDARD);
    }
    
    @Test
    @DisplayName("Should create pool ride")
    void shouldCreatePoolRide() {
        Location start = new Location(40.7128, -74.0060);
        Location end = new Location(40.7580, -73.9855);
        
        Ride ride = RideFactory.createRide(RideType.POOL, "rider1", start, end);
        
        assertThat(ride).isInstanceOf(PoolRide.class);
        assertThat(ride.getRideType()).isEqualTo(RideType.POOL);
    }
    
    @Test
    @DisplayName("Should create luxury ride")
    void shouldCreateLuxuryRide() {
        Location start = new Location(40.7128, -74.0060);
        Location end = new Location(40.7580, -73.9855);
        
        Ride ride = RideFactory.createRide(RideType.LUXURY, "rider1", start, end);
        
        assertThat(ride).isInstanceOf(LuxuryRide.class);
        assertThat(ride.getRideType()).isEqualTo(RideType.LUXURY);
    }
    
    @Test
    @DisplayName("Created ride should have correct properties")
    void createdRideShouldHaveCorrectProperties() {
        Location start = new Location(40.7128, -74.0060);
        Location end = new Location(40.7580, -73.9855);
        String riderId = "rider123";
        
        Ride ride = RideFactory.createRide(RideType.STANDARD, riderId, start, end);
        
        assertThat(ride.getRiderId()).isEqualTo(riderId);
        assertThat(ride.getStartLocation()).isEqualTo(start);
        assertThat(ride.getEndLocation()).isEqualTo(end);
        assertThat(ride.getId()).isNotNull();
    }
}
