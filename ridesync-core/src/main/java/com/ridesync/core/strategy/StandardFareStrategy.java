package com.ridesync.core.strategy;

/**
 * Standard fare calculation strategy.
 * Formula: (distance * baseFareRate + baseFee) * surgeMultiplier
 */
public class StandardFareStrategy implements FareStrategy {
    private static final double BASE_FEE = 20.0; // Flat booking fee
    private static final double MINIMUM_FARE = 30.0;
    
    @Override
    public double calculateFare(double distance, double baseFareRate, double surgeMultiplier) {
        double fare = (distance * baseFareRate + BASE_FEE) * surgeMultiplier;
        return Math.max(fare, MINIMUM_FARE); // Ensure minimum fare
    }
}
