package com.ridesync.persistence.mapper;

import com.ridesync.core.model.Location;
import com.ridesync.core.model.Rider;
import com.ridesync.persistence.entity.RiderEntity;

/**
 * Mapper for converting between Rider domain model and RiderEntity
 */
public class RiderMapper {
    
    public static RiderEntity toEntity(Rider rider) {
        if (rider == null) return null;
        
        return RiderEntity.builder()
            .id(rider.getId())
            .name(rider.getName())
            .phoneNumber(rider.getPhoneNumber())
            .email(rider.getEmail())
            .currentLatitude(rider.getCurrentLocation().latitude())
            .currentLongitude(rider.getCurrentLocation().longitude())
            .walletBalance(rider.getWalletBalance())
            .rating(rider.getRating())
            .build();
    }
    
    public static Rider toDomain(RiderEntity entity) {
        if (entity == null) return null;
        
        return Rider.builder()
            .id(entity.getId())
            .name(entity.getName())
            .phoneNumber(entity.getPhoneNumber())
            .email(entity.getEmail())
            .currentLocation(new Location(entity.getCurrentLatitude(), entity.getCurrentLongitude()))
            .walletBalance(entity.getWalletBalance())
            .rating(entity.getRating())
            .build();
    }
}
