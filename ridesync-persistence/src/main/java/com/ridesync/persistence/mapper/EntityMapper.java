package com.ridesync.persistence.mapper;

import com.ridesync.core.model.*;
import com.ridesync.persistence.entity.DriverEntity;
import com.ridesync.persistence.entity.RideEntity;
import com.ridesync.persistence.entity.RiderEntity;

/**
 * Mapper utility to convert between domain models and JPA entities.
 */
public class EntityMapper {
    
    /**
     * Convert Driver to DriverEntity.
     */
    public static DriverEntity toEntity(Driver driver) {
        return DriverEntity.builder()
                .id(driver.getId())
                .name(driver.getName())
                .vehicle(driver.getVehicle())
                .vehicleNumber(driver.getVehicleNumber())
                .status(driver.getStatus())
                .currentLatitude(driver.getCurrentLocation() != null ? 
                    driver.getCurrentLocation().getLatitude() : 0)
                .currentLongitude(driver.getCurrentLocation() != null ? 
                    driver.getCurrentLocation().getLongitude() : 0)
                .currentAddress(driver.getCurrentLocation() != null ? 
                    driver.getCurrentLocation().getAddress() : "")
                .totalEarnings(driver.getTotalEarnings())
                .rating(driver.getRating())
                .build();
    }
    
    /**
     * Convert DriverEntity to Driver.
     */
    public static Driver toDomain(DriverEntity entity) {
        return Driver.builder()
                .id(entity.getId())
                .name(entity.getName())
                .vehicle(entity.getVehicle())
                .vehicleNumber(entity.getVehicleNumber())
                .status(entity.getStatus())
                .currentLocation(Location.builder()
                    .latitude(entity.getCurrentLatitude())
                    .longitude(entity.getCurrentLongitude())
                    .address(entity.getCurrentAddress())
                    .build())
                .totalEarnings(entity.getTotalEarnings())
                .rating(entity.getRating())
                .build();
    }
    
    /**
     * Convert Rider to RiderEntity.
     */
    public static RiderEntity toEntity(Rider rider) {
        return RiderEntity.builder()
                .id(rider.getId())
                .name(rider.getName())
                .phone(rider.getPhone())
                .currentLatitude(rider.getCurrentLocation() != null ? 
                    rider.getCurrentLocation().getLatitude() : 0)
                .currentLongitude(rider.getCurrentLocation() != null ? 
                    rider.getCurrentLocation().getLongitude() : 0)
                .currentAddress(rider.getCurrentLocation() != null ? 
                    rider.getCurrentLocation().getAddress() : "")
                .rating(rider.getRating())
                .build();
    }
    
    /**
     * Convert RiderEntity to Rider.
     */
    public static Rider toDomain(RiderEntity entity) {
        return Rider.builder()
                .id(entity.getId())
                .name(entity.getName())
                .phone(entity.getPhone())
                .currentLocation(Location.builder()
                    .latitude(entity.getCurrentLatitude())
                    .longitude(entity.getCurrentLongitude())
                    .address(entity.getCurrentAddress())
                    .build())
                .rating(entity.getRating())
                .build();
    }
    
    /**
     * Convert Ride to RideEntity.
     */
    public static RideEntity toEntity(Ride ride) {
        return RideEntity.builder()
                .id(ride.getId())
                .riderId(ride.getRiderId())
                .driverId(ride.getDriverId())
                .rideType(ride.getRideType())
                .status(ride.getStatus())
                .startLatitude(ride.getStartLocation().getLatitude())
                .startLongitude(ride.getStartLocation().getLongitude())
                .startAddress(ride.getStartLocation().getAddress())
                .endLatitude(ride.getEndLocation().getLatitude())
                .endLongitude(ride.getEndLocation().getLongitude())
                .endAddress(ride.getEndLocation().getAddress())
                .distance(ride.getDistance())
                .fare(ride.getFare())
                .surgeMultiplier(ride.getSurgeMultiplier())
                .requestedAt(ride.getRequestedAt())
                .startedAt(ride.getStartedAt())
                .completedAt(ride.getCompletedAt())
                .build();
    }
}
