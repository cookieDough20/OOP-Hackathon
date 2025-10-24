package com.ridesync.core.strategy;

import com.ridesync.core.model.Ride;

/**
 * Luxury fare calculation strategy
 * Premium rides with higher rates - 150% of standard fare
 */
public class LuxuryFareStrategy implements FareStrategy {
    
    private static final double BASE_FARE = 100.0; // Premium base charge
    private static final double RATE_PER_KM = 18.0; // Premium rate per kilometer
    private static final double MINIMUM_FARE = 150.0; // Premium minimum
    private static final double LUXURY_PREMIUM_FACTOR = 1.5; // 50% premium
    
    @Override
    public double calculateFare(Ride ride, double distance, double surgeMultiplier) {
        double baseFare = BASE_FARE + (distance * RATE_PER_KM);
        double fareWithPremium = baseFare * LUXURY_PREMIUM_FACTOR;
        double fareWithSurge = fareWithPremium * surgeMultiplier;
        return Math.round(fareWithSurge * 100.0) / 100.0;
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
