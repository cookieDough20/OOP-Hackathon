package com.ridesync.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a rider (passenger) in the ride-sharing platform.
 * Maintains ride history and preferences.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rider {
    private String id;
    private String name;
    private String phone;
    private Location currentLocation;
    
    @Builder.Default
    private List<Ride> rideHistory = new ArrayList<>();
    
    @Builder.Default
    private double rating = 5.0;
    
    /**
     * Add a ride to the rider's history.
     */
    public void addRide(Ride ride) {
        rideHistory.add(ride);
    }
    
    /**
     * Get total number of completed rides.
     */
    public long getCompletedRidesCount() {
        return rideHistory.stream()
                .filter(ride -> ride.getStatus() == RideStatus.COMPLETED)
                .count();
    }
}
