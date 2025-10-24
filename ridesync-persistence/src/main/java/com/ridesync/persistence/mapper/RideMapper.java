package com.ridesync.persistence.mapper;

import com.ridesync.core.factory.RideFactory;
import com.ridesync.core.model.Location;
import com.ridesync.core.model.Ride;
import com.ridesync.persistence.entity.RideEntity;

/**
 * Mapper for converting between Ride domain model and RideEntity
 */
public class RideMapper {
    
    public static RideEntity toEntity(Ride ride) {
        if (ride == null) return null;
        
        return RideEntity.builder()
            .id(ride.getId())
            .riderId(ride.getRiderId())
            .driverId(ride.getDriverId())
            .rideType(ride.getRideType())
            .startLatitude(ride.getStartLocation().latitude())
            .startLongitude(ride.getStartLocation().longitude())
            .endLatitude(ride.getEndLocation().latitude())
            .endLongitude(ride.getEndLocation().longitude())
            .distance(ride.getDistance())
            .fare(ride.getFare())
            .status(ride.getStatus())
            .requestedAt(ride.getRequestedAt())
            .startedAt(ride.getStartedAt())
            .completedAt(ride.getCompletedAt())
            .build();
    }
    
    public static Ride toDomain(RideEntity entity) {
        if (entity == null) return null;
        
        Ride ride = RideFactory.createRide(
            entity.getRideType(),
            entity.getRiderId(),
            new Location(entity.getStartLatitude(), entity.getStartLongitude()),
            new Location(entity.getEndLatitude(), entity.getEndLongitude())
        );
        
        ride.setId(entity.getId());
        ride.setDriverId(entity.getDriverId());
        ride.setDistance(entity.getDistance());
        ride.setFare(entity.getFare());
        ride.setStatus(entity.getStatus());
        ride.setRequestedAt(entity.getRequestedAt());
        ride.setStartedAt(entity.getStartedAt());
        ride.setCompletedAt(entity.getCompletedAt());
        
        return ride;
    }
}
