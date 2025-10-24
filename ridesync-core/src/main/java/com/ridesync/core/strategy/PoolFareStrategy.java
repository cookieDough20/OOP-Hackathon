package com.ridesync.core.strategy;

import com.ridesync.core.model.Ride;

/**
 * Pool fare calculation strategy
 * Shared rides are cheaper - 70% of standard fare
 */
public class PoolFareStrategy implements FareStrategy {
    
    private static final double BASE_FARE = 30.0; // Lower base for shared rides
    private static final double RATE_PER_KM = 8.0; // Lower rate per kilometer
    private static final double MINIMUM_FARE = 50.0; // Lower minimum
    private static final double POOL_DISCOUNT_FACTOR = 0.7; // 30% discount
    
    @Override
    public double calculateFare(Ride ride, double distance, double surgeMultiplier) {
        double baseFare = BASE_FARE + (distance * RATE_PER_KM);
        double fareWithDiscount = baseFare * POOL_DISCOUNT_FACTOR;
        double fareWithSurge = fareWithDiscount * surgeMultiplier;
        return Math.max(MINIMUM_FARE, Math.round(fareWithSurge * 100.0) / 100.0);
    }
    
    @Override
    public double getBaseRatePerKm() {
        return RATE_PER_KM;
    }
    
    @Override
    public double getMinimumFare() {
        return MINIMUM_FARE;
    }
}
