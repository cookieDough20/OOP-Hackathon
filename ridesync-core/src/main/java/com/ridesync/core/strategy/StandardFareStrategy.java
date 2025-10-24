package com.ridesync.core.strategy;

import com.ridesync.core.model.Ride;

/**
 * Standard fare calculation strategy
 * Formula: max(minimumFare, (baseFare + distance * ratePerKm) * surgeMultiplier)
 */
public class StandardFareStrategy implements FareStrategy {
    
    private static final double BASE_FARE = 50.0; // Base charge in currency units
    private static final double RATE_PER_KM = 12.0; // Rate per kilometer
    private static final double MINIMUM_FARE = 70.0; // Minimum charge
    
    @Override
    public double calculateFare(Ride ride, double distance, double surgeMultiplier) {
        double baseFare = BASE_FARE + (distance * RATE_PER_KM);
        double fareWithSurge = baseFare * surgeMultiplier;
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
