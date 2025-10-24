package com.ridesync.core.model;

import com.ridesync.core.model.enums.RideType;
import lombok.NoArgsConstructor;

/**
 * Standard ride implementation for regular solo passengers
 */
@NoArgsConstructor
public class StandardRide extends Ride {
    
    @Override
    public RideType getRideType() {
        return RideType.STANDARD;
    }
}
