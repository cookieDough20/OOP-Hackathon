package com.ridesync.core.model;

import com.ridesync.core.strategy.FareStrategy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Pool ride for sharing with other passengers.
 * Lower fare rate due to cost sharing.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PoolRide extends Ride {
    private static final double BASE_FARE_RATE = 6.0; // Lower rate for shared rides
    
    private List<String> pooledRiderIds = new ArrayList<>(); // Other riders in the pool
    private int maxPoolSize = 4; // Maximum passengers
    
    @Override
    public double calculateFare(FareStrategy fareStrategy, double distance, double surgeMultiplier) {
        // Apply pool discount based on number of riders
        double poolDiscount = pooledRiderIds.isEmpty() ? 1.0 : 0.8;
        double calculatedFare = fareStrategy.calculateFare(distance, BASE_FARE_RATE, surgeMultiplier) * poolDiscount;
        setFare(calculatedFare);
        return calculatedFare;
    }
    
    @Override
    public double getBaseFareRate() {
        return BASE_FARE_RATE;
    }
    
    /**
     * Check if pool has capacity for more riders.
     */
    public boolean hasCapacity() {
        return pooledRiderIds.size() < maxPoolSize;
    }
}
