package com.ridesync.api.service;

import com.ridesync.api.dto.RiderRequest;
import com.ridesync.core.model.Location;
import com.ridesync.core.model.Rider;
import com.ridesync.persistence.entity.RiderEntity;
import com.ridesync.persistence.mapper.EntityMapper;
import com.ridesync.persistence.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for rider management operations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RiderService {
    
    private final RiderRepository riderRepository;
    
    /**
     * Register a new rider.
     */
    @Transactional
    public Rider registerRider(RiderRequest request) {
        String riderId = "RDR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Rider rider = Rider.builder()
                .id(riderId)
                .name(request.getName())
                .phone(request.getPhone())
                .currentLocation(Location.builder()
                    .latitude(request.getLatitude() != null ? request.getLatitude() : 0.0)
                    .longitude(request.getLongitude() != null ? request.getLongitude() : 0.0)
                    .address(request.getAddress())
                    .build())
                .rating(5.0)
                .build();
        
        RiderEntity entity = EntityMapper.toEntity(rider);
        riderRepository.save(entity);
        
        log.info("Registered new rider: {} with ID: {}", rider.getName(), rider.getId());
        return rider;
    }
    
    /**
     * Get rider by ID.
     */
    public Rider getRider(String riderId) {
        RiderEntity entity = riderRepository.findById(riderId)
                .orElseThrow(() -> new RuntimeException("Rider not found: " + riderId));
        return EntityMapper.toDomain(entity);
    }
    
    /**
     * Get all riders.
     */
    public List<Rider> getAllRiders() {
        return riderRepository.findAll().stream()
                .map(EntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
