package com.ridesync.core.strategy;

import com.ridesync.core.model.Ride;

/**
 * Strategy pattern interface for fare calculation
 * Allows flexible fare computation algorithms
 * Demonstrates Strategy Design Pattern for Open-Closed Principle
 */
public interface FareStrategy {
    
    /**
     * Calculate fare for a ride
     * 
     * @param ride The ride to calculate fare for
     * @param distance Distance in kilometers
     * @param surgeMultiplier Dynamic surge pricing multiplier (1.0 = no surge)
     * @return Calculated fare amount
     */
    double calculateFare(Ride ride, double distance, double surgeMultiplier);
    
    /**
     * Get the base rate per kilometer for this strategy
     */
    double getBaseRatePerKm();
    
    /**
     * Get the minimum fare for this strategy
     */
    double getMinimumFare();
}
