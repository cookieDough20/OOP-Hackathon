package com.ridesync.core.strategy;

/**
 * Luxury ride fare calculation strategy.
 * Higher base fee and premium rate for luxury service.
 */
public class LuxuryFareStrategy implements FareStrategy {
    private static final double BASE_FEE = 50.0; // Premium booking fee
    private static final double MINIMUM_FARE = 100.0;
    private static final double LUXURY_PREMIUM = 1.2; // 20% luxury premium
    
    @Override
    public double calculateFare(double distance, double baseFareRate, double surgeMultiplier) {
        double fare = (distance * baseFareRate + BASE_FEE) * surgeMultiplier * LUXURY_PREMIUM;
        return Math.max(fare, MINIMUM_FARE);
    }
}
