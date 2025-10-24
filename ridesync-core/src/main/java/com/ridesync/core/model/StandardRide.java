package com.ridesync.core.model;

import com.ridesync.core.strategy.FareStrategy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Standard ride with regular pricing.
 * Most common ride type with balanced fare rates.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StandardRide extends Ride {
    private static final double BASE_FARE_RATE = 10.0; // Base fare per km
    
    @Override
    public double calculateFare(FareStrategy fareStrategy, double distance, double surgeMultiplier) {
        double calculatedFare = fareStrategy.calculateFare(distance, BASE_FARE_RATE, surgeMultiplier);
        setFare(calculatedFare);
        return calculatedFare;
    }
    
    @Override
    public double getBaseFareRate() {
        return BASE_FARE_RATE;
    }
}
