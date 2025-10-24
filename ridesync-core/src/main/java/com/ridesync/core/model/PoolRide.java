package com.ridesync.core.model;

import com.ridesync.core.model.enums.RideType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Pool ride implementation for shared rides with multiple passengers
 * Cheaper fare due to ride sharing
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PoolRide extends Ride {
    
    private List<String> coPassengerIds = new ArrayList<>();
    private int maxPassengers = 3;
    
    @Override
    public RideType getRideType() {
        return RideType.POOL;
    }
    
    /**
     * Add a co-passenger to the pool ride
     */
    public boolean addCoPassenger(String passengerId) {
        if (coPassengerIds.size() < maxPassengers) {
            coPassengerIds.add(passengerId);
            return true;
        }
        return false;
    }
    
    /**
     * Check if pool has capacity for more passengers
     */
    public boolean hasCapacity() {
        return coPassengerIds.size() < maxPassengers;
    }
}
