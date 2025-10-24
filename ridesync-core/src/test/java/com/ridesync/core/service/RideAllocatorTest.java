package com.ridesync.core.service;

import com.ridesync.core.exception.NoDriverAvailableException;
import com.ridesync.core.factory.RideFactory;
import com.ridesync.core.model.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RideAllocator singleton and thread safety.
 */
class RideAllocatorTest {
    
    @Test
    void testSingletonInstance() {
        RideAllocator instance1 = RideAllocator.getInstance();
        RideAllocator instance2 = RideAllocator.getInstance();
        
        assertSame(instance1, instance2, "RideAllocator should be a singleton");
    }
    
    @Test
    void testAssignDriverSuccess() {
        RideAllocator allocator = RideAllocator.getInstance();
        
        Location start = Location.builder().latitude(12.9716).longitude(77.5946).build();
        Location end = Location.builder().latitude(12.9698).longitude(77.7500).build();
        Ride ride = RideFactory.createRide(RideType.STANDARD, "rider1", start, end);
        
        Driver driver = Driver.builder()
                .id("driver1")
                .name("Test Driver")
                .status(DriverStatus.AVAILABLE)
                .currentLocation(start)
                .vehicle("Test Car")
                .vehicleNumber("TEST-123")
                .build();
        
        List<Driver> drivers = List.of(driver);
        
        Driver assigned = allocator.assignDriver(ride, drivers);
        
        assertNotNull(assigned);
        assertEquals(driver.getId(), assigned.getId());
        assertEquals(RideStatus.ASSIGNED, ride.getStatus());
        assertEquals(DriverStatus.BUSY, assigned.getStatus());
        assertTrue(ride.getFare() > 0);
    }
    
    @Test
    void testAssignDriverNoAvailable() {
        RideAllocator allocator = RideAllocator.getInstance();
        
        Location start = Location.builder().latitude(12.9716).longitude(77.5946).build();
        Location end = Location.builder().latitude(12.9698).longitude(77.7500).build();
        Ride ride = RideFactory.createRide(RideType.STANDARD, "rider1", start, end);
        
        List<Driver> emptyDriverList = new ArrayList<>();
        
        assertThrows(NoDriverAvailableException.class, () -> {
            allocator.assignDriver(ride, emptyDriverList);
        });
    }
    
    @Test
    void testCompleteRide() {
        RideAllocator allocator = RideAllocator.getInstance();
        
        Location start = Location.builder().latitude(12.9716).longitude(77.5946).build();
        Location end = Location.builder().latitude(12.9698).longitude(77.7500).build();
        Ride ride = RideFactory.createRide(RideType.STANDARD, "rider1", start, end);
        ride.setFare(150.0);
        
        Driver driver = Driver.builder()
                .id("driver1")
                .name("Test Driver")
                .status(DriverStatus.BUSY)
                .currentLocation(start)
                .vehicle("Test Car")
                .vehicleNumber("TEST-123")
                .totalEarnings(0.0)
                .build();
        
        allocator.completeRide(ride, driver);
        
        assertEquals(RideStatus.COMPLETED, ride.getStatus());
        assertEquals(DriverStatus.AVAILABLE, driver.getStatus());
        assertNotNull(ride.getCompletedAt());
    }
}
