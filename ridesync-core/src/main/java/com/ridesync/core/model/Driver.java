package com.ridesync.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a driver in the ride-sharing platform.
 * Maintains ride history and earnings tracking.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    private String id;
    private String name;
    private String vehicle;
    private String vehicleNumber;
    private DriverStatus status;
    private Location currentLocation;
    
    @Builder.Default
    private double totalEarnings = 0.0;
    
    @Builder.Default
    private List<Ride> rideHistory = new ArrayList<>();
    
    @Builder.Default
    private double rating = 5.0;
    
    /**
     * Add a completed ride to history and update earnings.
     */
    public void addCompletedRide(Ride ride) {
        if (ride.getStatus() == RideStatus.COMPLETED) {
            rideHistory.add(ride);
            totalEarnings += ride.getFare();
        }
    }
    
    /**
     * Calculate total earnings from completed rides using streams.
     */
    public double calculateTotalEarnings() {
        return rideHistory.stream()
                .filter(ride -> ride.getStatus() == RideStatus.COMPLETED)
                .mapToDouble(Ride::getFare)
                .sum();
    }
    
    /**
     * Check if driver is available for a new ride.
     */
    public boolean isAvailable() {
        return status == DriverStatus.AVAILABLE;
    }
}
