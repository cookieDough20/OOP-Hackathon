package com.ridesync.core.factory;

import com.ridesync.core.model.RideType;
import com.ridesync.core.strategy.FareStrategy;
import com.ridesync.core.strategy.LuxuryFareStrategy;
import com.ridesync.core.strategy.PoolFareStrategy;
import com.ridesync.core.strategy.StandardFareStrategy;

/**
 * Factory for creating appropriate fare strategy based on ride type.
 */
public class FareStrategyFactory {
    
    /**
     * Get the appropriate fare strategy for the given ride type.
     */
    public static FareStrategy getStrategy(RideType rideType) {
        return switch (rideType) {
            case STANDARD -> new StandardFareStrategy();
            case POOL -> new PoolFareStrategy();
            case LUXURY -> new LuxuryFareStrategy();
        };
    }
}
