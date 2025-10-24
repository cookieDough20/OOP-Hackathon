package com.ridesync.core.strategy;

/**
 * Pool ride fare calculation strategy.
 * Lower base fee and discounted rate for shared rides.
 */
public class PoolFareStrategy implements FareStrategy {
    private static final double BASE_FEE = 10.0; // Lower flat fee for pools
    private static final double MINIMUM_FARE = 20.0;
    
    @Override
    public double calculateFare(double distance, double baseFareRate, double surgeMultiplier) {
        double fare = (distance * baseFareRate + BASE_FEE) * surgeMultiplier;
        return Math.max(fare, MINIMUM_FARE);
    }
}
