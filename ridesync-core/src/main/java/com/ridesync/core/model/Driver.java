package com.ridesync.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ridesync.core.model.enums.DriverStatus;
import com.ridesync.core.model.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Driver entity representing a driver in the ride-sharing platform
 * Demonstrates encapsulation with private fields and Lombok annotations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    
    @NotBlank(message = "Driver name is required")
    private String name;
    
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    
    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;
    
    @NotBlank(message = "License plate is required")
    private String licensePlate;
    
    @NotNull(message = "Current location is required")
    private Location currentLocation;
    
    @Builder.Default
    private DriverStatus status = DriverStatus.AVAILABLE;
    
    @Builder.Default
    private double earnings = 0.0;
    
    @Builder.Default
    private double rating = 5.0;
    
    @Builder.Default
    private int totalRides = 0;
    
    @JsonIgnore
    @Builder.Default
    private List<Ride> rideHistory = new ArrayList<>();
    
    /**
     * Add a completed ride to driver's history and update earnings
     */
    public void completeRide(Ride ride) {
        this.rideHistory.add(ride);
        this.earnings += ride.getFare();
        this.totalRides++;
        this.status = DriverStatus.AVAILABLE;
    }
    
    /**
     * Mark driver as busy when assigned to a ride
     */
    public void assignToRide() {
        this.status = DriverStatus.BUSY;
    }
    
    /**
     * Check if driver is available for assignment
     */
    public boolean isAvailable() {
        return this.status == DriverStatus.AVAILABLE;
    }
}
