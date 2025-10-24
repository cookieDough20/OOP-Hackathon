package com.ridesync.core.strategy;

/**
 * Strategy interface for fare calculation.
 * Implements the Strategy Design Pattern for flexible fare algorithms.
 * Different implementations can provide various pricing models.
 */
public interface FareStrategy {
    /**
     * Calculate fare based on distance, base rate, and surge multiplier.
     * 
     * @param distance Distance of the ride in kilometers
     * @param baseFareRate Base fare rate per kilometer
     * @param surgeMultiplier Current surge pricing multiplier (1.0 = no surge)
     * @return Calculated fare amount
     */
    double calculateFare(double distance, double baseFareRate, double surgeMultiplier);
}
