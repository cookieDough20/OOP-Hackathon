package com.ridesync.core.factory;

import com.ridesync.core.model.*;
import com.ridesync.core.model.enums.RideType;

/**
 * Factory pattern implementation for creating different types of rides
 * Demonstrates Factory Design Pattern for object creation
 * Provides loose coupling and easy extensibility
 */
public class RideFactory {
    
    /**
     * Create a ride based on the ride type
     * 
     * @param rideType Type of ride to create
     * @param riderId ID of the rider requesting the ride
     * @param startLocation Starting location
     * @param endLocation Destination location
     * @return Appropriate Ride subclass instance
     * @throws IllegalArgumentException if ride type is unknown
     */
    public static Ride createRide(RideType rideType, String riderId, 
                                  Location startLocation, Location endLocation) {
        Ride ride = switch (rideType) {
            case STANDARD -> new StandardRide();
            case POOL -> new PoolRide();
            case LUXURY -> new LuxuryRide();
        };
        
        ride.setRiderId(riderId);
        ride.setStartLocation(startLocation);
        ride.setEndLocation(endLocation);
        
        return ride;
    }
    
    /**
     * Create a ride using reflection (demonstrates reflection usage)
     * 
     * @param rideTypeString String representation of ride type
     * @param riderId ID of the rider
     * @param startLocation Starting location
     * @param endLocation Destination location
     * @return Created ride instance
     */
    public static Ride createRideWithReflection(String rideTypeString, String riderId,
                                                 Location startLocation, Location endLocation) {
        try {
            RideType rideType = RideType.valueOf(rideTypeString.toUpperCase());
            return createRide(rideType, riderId, startLocation, endLocation);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ride type: " + rideTypeString, e);
        }
    }
}
