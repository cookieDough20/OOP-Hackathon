package com.ridesync.api.service;

import com.ridesync.api.dto.RegisterRiderRequest;
import com.ridesync.core.model.Location;
import com.ridesync.core.model.Rider;
import com.ridesync.persistence.mapper.RiderMapper;
import com.ridesync.persistence.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for rider management operations
 */
@Service
@RequiredArgsConstructor
public class RiderService {
    
    private final RiderRepository riderRepository;
    
    /**
     * Register a new rider
     */
    @Transactional
    public Rider registerRider(RegisterRiderRequest request) {
        Rider rider = Rider.builder()
            .name(request.getName())
            .phoneNumber(request.getPhoneNumber())
            .email(request.getEmail())
            .currentLocation(new Location(request.getCurrentLatitude(), request.getCurrentLongitude()))
            .build();
        
        var saved = riderRepository.save(RiderMapper.toEntity(rider));
        return RiderMapper.toDomain(saved);
    }
    
    /**
     * Get rider by ID
     */
    public Rider getRider(String id) {
        return riderRepository.findById(id)
            .map(RiderMapper::toDomain)
            .orElseThrow(() -> new RuntimeException("Rider not found with ID: " + id));
    }
    
    /**
     * Get all riders
     */
    public List<Rider> getAllRiders() {
        return riderRepository.findAll()
            .stream()
            .map(RiderMapper::toDomain)
            .collect(Collectors.toList());
    }
}
