package com.ridesync.core.service;

import com.ridesync.core.exception.NoDriverAvailableException;
import com.ridesync.core.factory.RideFactory;
import com.ridesync.core.model.Driver;
import com.ridesync.core.model.Location;
import com.ridesync.core.model.Ride;
import com.ridesync.core.model.enums.DriverStatus;
import com.ridesync.core.model.enums.RideType;
import com.ridesync.core.model.enums.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Ride Allocator Tests")
class RideAllocatorTest {
    
    private RideAllocator allocator;
    private List<Driver> drivers;
    
    @BeforeEach
    void setUp() {
        allocator = RideAllocator.getInstance();
        drivers = createSampleDrivers();
    }
    
    @Test
    @DisplayName("Should allocate nearest available driver")
    void shouldAllocateNearestDriver() {
        Location rideStart = new Location(40.7128, -74.0060);
        Location rideEnd = new Location(40.7580, -73.9855);
        Ride ride = RideFactory.createRide(RideType.STANDARD, "rider1", rideStart, rideEnd);
        
        Driver allocated = allocator.allocateDriver(ride, drivers);
        
        assertThat(allocated).isNotNull();
        assertThat(allocated.getStatus()).isEqualTo(DriverStatus.BUSY);
        assertThat(ride.getDriverId()).isEqualTo(allocated.getId());
    }
    
    @Test
    @DisplayName("Should throw exception when no drivers available")
    void shouldThrowExceptionWhenNoDriversAvailable() {
        List<Driver> busyDrivers = new ArrayList<>();
        drivers.forEach(d -> d.setStatus(DriverStatus.BUSY));
        
        Location rideStart = new Location(40.7128, -74.0060);
        Location rideEnd = new Location(40.7580, -73.9855);
        Ride ride = RideFactory.createRide(RideType.STANDARD, "rider1", rideStart, rideEnd);
        
        assertThatThrownBy(() -> allocator.allocateDriver(ride, drivers))
            .isInstanceOf(NoDriverAvailableException.class);
    }
    
    @Test
    @DisplayName("Allocator should be singleton")
    void shouldBeSingleton() {
        RideAllocator instance1 = RideAllocator.getInstance();
        RideAllocator instance2 = RideAllocator.getInstance();
        
        assertThat(instance1).isSameAs(instance2);
    }
    
    private List<Driver> createSampleDrivers() {
        List<Driver> driverList = new ArrayList<>();
        
        driverList.add(Driver.builder()
            .name("Driver 1")
            .phoneNumber("123")
            .vehicleType(VehicleType.SEDAN)
            .licensePlate("ABC123")
            .currentLocation(new Location(40.7128, -74.0060))
            .status(DriverStatus.AVAILABLE)
            .build());
        
        driverList.add(Driver.builder()
            .name("Driver 2")
            .phoneNumber("456")
            .vehicleType(VehicleType.SUV)
            .licensePlate("XYZ789")
            .currentLocation(new Location(40.7580, -73.9855))
            .status(DriverStatus.AVAILABLE)
            .build());
        
        return driverList;
    }
}
