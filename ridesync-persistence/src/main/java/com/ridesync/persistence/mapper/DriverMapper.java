package com.ridesync.persistence.mapper;

import com.ridesync.core.model.Driver;
import com.ridesync.core.model.Location;
import com.ridesync.persistence.entity.DriverEntity;

/**
 * Mapper for converting between Driver domain model and DriverEntity
 */
public class DriverMapper {
    
    public static DriverEntity toEntity(Driver driver) {
        if (driver == null) return null;
        
        return DriverEntity.builder()
            .id(driver.getId())
            .name(driver.getName())
            .phoneNumber(driver.getPhoneNumber())
            .vehicleType(driver.getVehicleType())
            .licensePlate(driver.getLicensePlate())
            .currentLatitude(driver.getCurrentLocation().latitude())
            .currentLongitude(driver.getCurrentLocation().longitude())
            .status(driver.getStatus())
            .earnings(driver.getEarnings())
            .rating(driver.getRating())
            .totalRides(driver.getTotalRides())
            .build();
    }
    
    public static Driver toDomain(DriverEntity entity) {
        if (entity == null) return null;
        
        return Driver.builder()
            .id(entity.getId())
            .name(entity.getName())
            .phoneNumber(entity.getPhoneNumber())
            .vehicleType(entity.getVehicleType())
            .licensePlate(entity.getLicensePlate())
            .currentLocation(new Location(entity.getCurrentLatitude(), entity.getCurrentLongitude()))
            .status(entity.getStatus())
            .earnings(entity.getEarnings())
            .rating(entity.getRating())
            .totalRides(entity.getTotalRides())
            .build();
    }
}
