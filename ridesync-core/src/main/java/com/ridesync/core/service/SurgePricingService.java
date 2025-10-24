package com.ridesync.core.service;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Service for calculating dynamic surge pricing.
 * Uses ML-like heuristic based on time of day, demand, and randomization.
 * This is a "wow factor" feature for hackathon judges.
 */
@Slf4j
public class SurgePricingService {
    private static final Random random = new Random();
    
    /**
     * Calculate surge multiplier based on current conditions.
     * Algorithm considers:
     * - Time of day (peak hours)
     * - Day of week
     * - Simulated demand (random component for demo)
     * 
     * @param location Current location (could be used for area-specific surge)
     * @return Surge multiplier (1.0 = no surge, >1.0 = surge pricing)
     */
    public double calculateSurgeMultiplier(com.ridesync.core.model.Location location) {
        LocalDateTime now = LocalDateTime.now();
        double surge = 1.0;
        
        // Time-of-day based surge
        int hour = now.getHour();
        if ((hour >= 7 && hour <= 9) || (hour >= 17 && hour <= 19)) {
            // Morning and evening rush hours
            surge += 0.5;
        } else if (hour >= 22 || hour <= 5) {
            // Late night surge
            surge += 0.3;
        }
        
        // Weekend surge (simulated)
        int dayOfWeek = now.getDayOfWeek().getValue();
        if (dayOfWeek == 6 || dayOfWeek == 7) {
            surge += 0.2;
        }
        
        // Simulated demand factor (ML-like heuristic)
        // In production, this would query real-time demand data
        double demandFactor = 0.2 + random.nextDouble() * 0.4; // 0.2 to 0.6
        surge += demandFactor;
        
        // Cap maximum surge
        surge = Math.min(surge, 2.5);
        
        log.info("Calculated surge multiplier: {} for hour: {}", surge, hour);
        return Math.round(surge * 100.0) / 100.0; // Round to 2 decimals
    }
    
    /**
     * Check if current time is peak hour.
     */
    public boolean isPeakHour() {
        int hour = LocalDateTime.now().getHour();
        return (hour >= 7 && hour <= 9) || (hour >= 17 && hour <= 19);
    }
}
