package com.ridesync.core.service;

import com.ridesync.core.model.Location;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Service for calculating dynamic surge pricing
 * WOW Factor: ML-like heuristic based on time, location, and demand
 */
public class SurgePricingService {
    
    private final Random random = new Random();
    
    /**
     * Calculate surge multiplier based on various factors
     * 
     * @param location Current location
     * @param time Current time
     * @param demandFactor Number of active rides (simulated demand)
     * @return Surge multiplier (1.0 = no surge, >1.0 = surge pricing)
     */
    public double calculateSurgeMultiplier(Location location, LocalDateTime time, int demandFactor) {
        double surgeMultiplier = 1.0;
        
        // Time-based surge (peak hours)
        int hour = time.getHour();
        if ((hour >= 7 && hour <= 9) || (hour >= 17 && hour <= 19)) {
            surgeMultiplier += 0.3; // Morning and evening rush
        }
        if (hour >= 22 || hour <= 2) {
            surgeMultiplier += 0.5; // Late night surge
        }
        
        // Demand-based surge
        if (demandFactor > 50) {
            surgeMultiplier += 0.2;
        } else if (demandFactor > 100) {
            surgeMultiplier += 0.4;
        }
        
        // Location-based surge (simulate busy areas)
        // High latitude/longitude values simulate busy city centers
        if (Math.abs(location.latitude()) > 40 && Math.abs(location.longitude()) > 70) {
            surgeMultiplier += 0.2;
        }
        
        // Add small random variation (0.0 to 0.15) for ML-like behavior
        surgeMultiplier += random.nextDouble() * 0.15;
        
        // Cap maximum surge at 2.5x
        return Math.min(surgeMultiplier, 2.5);
    }
    
    /**
     * Get current surge multiplier for a location
     */
    public double getCurrentSurgeMultiplier(Location location, int activeDemand) {
        return calculateSurgeMultiplier(location, LocalDateTime.now(), activeDemand);
    }
}
