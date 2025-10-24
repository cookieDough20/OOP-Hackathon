package com.ridesync.core.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for fare calculation strategies.
 */
class FareStrategyTest {
    
    @Test
    void testStandardFareCalculation() {
        FareStrategy strategy = new StandardFareStrategy();
        double distance = 10.0; // km
        double baseFareRate = 10.0; // per km
        double surgeMultiplier = 1.0; // no surge
        
        double fare = strategy.calculateFare(distance, baseFareRate, surgeMultiplier);
        
        // (10 km * 10/km + 20 base fee) * 1.0 = 120
        assertEquals(120.0, fare, 0.01, "Standard fare calculation incorrect");
    }
    
    @Test
    void testStandardFareWithSurge() {
        FareStrategy strategy = new StandardFareStrategy();
        double distance = 10.0;
        double baseFareRate = 10.0;
        double surgeMultiplier = 1.5;
        
        double fare = strategy.calculateFare(distance, baseFareRate, surgeMultiplier);
        
        // (10 * 10 + 20) * 1.5 = 180
        assertEquals(180.0, fare, 0.01, "Surge pricing calculation incorrect");
    }
    
    @Test
    void testPoolFareCalculation() {
        FareStrategy strategy = new PoolFareStrategy();
        double distance = 10.0;
        double baseFareRate = 6.0;
        double surgeMultiplier = 1.0;
        
        double fare = strategy.calculateFare(distance, baseFareRate, surgeMultiplier);
        
        // (10 * 6 + 10) * 1.0 = 70
        assertEquals(70.0, fare, 0.01, "Pool fare calculation incorrect");
    }
    
    @Test
    void testLuxuryFareCalculation() {
        FareStrategy strategy = new LuxuryFareStrategy();
        double distance = 10.0;
        double baseFareRate = 20.0;
        double surgeMultiplier = 1.0;
        
        double fare = strategy.calculateFare(distance, baseFareRate, surgeMultiplier);
        
        // (10 * 20 + 50) * 1.0 * 1.2 = 300
        assertEquals(300.0, fare, 0.01, "Luxury fare calculation incorrect");
    }
    
    @Test
    void testMinimumFareEnforced() {
        FareStrategy strategy = new StandardFareStrategy();
        double distance = 0.5; // Very short distance
        double baseFareRate = 10.0;
        double surgeMultiplier = 1.0;
        
        double fare = strategy.calculateFare(distance, baseFareRate, surgeMultiplier);
        
        // Should enforce minimum fare of 30
        assertEquals(30.0, fare, 0.01, "Minimum fare not enforced");
    }
}
