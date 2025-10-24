package com.ridesync.core.factory;

import com.ridesync.core.model.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Factory Pattern implementation for creating different ride types.
 * Encapsulates ride creation logic and ensures proper initialization.
 */
public class RideFactory {
    
    /**
     * Create a ride based on the specified type.
     * 
     * @param rideType Type of ride to create
     * @param riderId ID of the rider requesting the ride
     * @param startLocation Starting location
     * @param endLocation Destination location
     * @return A new Ride instance of the appropriate type
     * @throws IllegalArgumentException if ride type is unknown
     */
    public static Ride createRide(RideType rideType, String riderId, 
                                   Location startLocation, Location endLocation) {
        String rideId = generateRideId();
        LocalDateTime now = LocalDateTime.now();
        double distance = startLocation.distanceTo(endLocation);
        
        return switch (rideType) {
            case STANDARD -> StandardRide.builder()
                    .id(rideId)
                    .riderId(riderId)
                    .rideType(RideType.STANDARD)
                    .startLocation(startLocation)
                    .endLocation(endLocation)
                    .status(RideStatus.REQUESTED)
                    .requestedAt(now)
                    .distance(distance)
                    .surgeMultiplier(1.0)
                    .build();
                    
            case POOL -> PoolRide.builder()
                    .id(rideId)
                    .riderId(riderId)
                    .rideType(RideType.POOL)
                    .startLocation(startLocation)
                    .endLocation(endLocation)
                    .status(RideStatus.REQUESTED)
                    .requestedAt(now)
                    .distance(distance)
                    .surgeMultiplier(1.0)
                    .maxPoolSize(4)
                    .build();
                    
            case LUXURY -> LuxuryRide.builder()
                    .id(rideId)
                    .riderId(riderId)
                    .rideType(RideType.LUXURY)
                    .startLocation(startLocation)
                    .endLocation(endLocation)
                    .status(RideStatus.REQUESTED)
                    .requestedAt(now)
                    .distance(distance)
                    .surgeMultiplier(1.0)
                    .amenities("WiFi, Water, Premium Music")
                    .isPremiumDriver(true)
                    .build();
        };
    }
    
    /**
     * Generate a unique ride ID.
     */
    private static String generateRideId() {
        return "RIDE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Create a ride with custom surge multiplier (for dynamic pricing).
     */
    public static Ride createRideWithSurge(RideType rideType, String riderId,
                                           Location startLocation, Location endLocation,
                                           double surgeMultiplier) {
        Ride ride = createRide(rideType, riderId, startLocation, endLocation);
        ride.setSurgeMultiplier(surgeMultiplier);
        return ride;
    }
}
