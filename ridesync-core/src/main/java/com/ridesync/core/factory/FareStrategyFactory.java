package com.ridesync.core.factory;

import com.ridesync.core.model.enums.RideType;
import com.ridesync.core.strategy.*;

/**
 * Factory for creating appropriate fare calculation strategies
 * Combines Factory and Strategy patterns
 */
public class FareStrategyFactory {
    
    /**
     * Get the appropriate fare strategy for a ride type
     * 
     * @param rideType Type of ride
     * @return Corresponding fare strategy
     */
    public static FareStrategy getStrategy(RideType rideType) {
        return switch (rideType) {
            case STANDARD -> new StandardFareStrategy();
            case POOL -> new PoolFareStrategy();
            case LUXURY -> new LuxuryFareStrategy();
        };
    }
}
