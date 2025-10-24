package com.ridesync.core.model;

import com.ridesync.core.strategy.FareStrategy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Luxury ride with premium vehicles and higher comfort.
 * Higher fare rate for premium service.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LuxuryRide extends Ride {
    private static final double BASE_FARE_RATE = 20.0; // Premium rate
    
    private String amenities; // e.g., "WiFi, Water, Premium Music"
    private boolean isPremiumDriver; // Driver has premium certification
    
    @Override
    public double calculateFare(FareStrategy fareStrategy, double distance, double surgeMultiplier) {
        // Add luxury premium
        double luxuryPremium = isPremiumDriver ? 1.1 : 1.0;
        double calculatedFare = fareStrategy.calculateFare(distance, BASE_FARE_RATE, surgeMultiplier) * luxuryPremium;
        setFare(calculatedFare);
        return calculatedFare;
    }
    
    @Override
    public double getBaseFareRate() {
        return BASE_FARE_RATE;
    }
}
