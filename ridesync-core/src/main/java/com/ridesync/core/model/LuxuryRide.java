package com.ridesync.core.model;

import com.ridesync.core.model.enums.RideType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Luxury ride implementation for premium experience
 * Higher base fare and premium amenities
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class LuxuryRide extends Ride {
    
    private boolean requestedRefreshments = false;
    private boolean requestedNewspaper = false;
    private String preferredMusic;
    
    @Override
    public RideType getRideType() {
        return RideType.LUXURY;
    }
}
