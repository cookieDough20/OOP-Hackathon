package com.ridesync.api.service;

import com.ridesync.api.dto.AnalyticsResponse;
import com.ridesync.core.model.RideStatus;
import com.ridesync.core.model.RideType;
import com.ridesync.persistence.entity.RideEntity;
import com.ridesync.persistence.repository.DriverRepository;
import com.ridesync.persistence.repository.RideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AnalyticsService Stream operations.
 */
@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {
    
    @Mock
    private RideRepository rideRepository;
    
    @Mock
    private DriverRepository driverRepository;
    
    @InjectMocks
    private AnalyticsService analyticsService;
    
    private List<RideEntity> sampleRides;
    
    @BeforeEach
    void setUp() {
        sampleRides = List.of(
            createRideEntity("ride1", RideStatus.COMPLETED, RideType.STANDARD, 100.0),
            createRideEntity("ride2", RideStatus.COMPLETED, RideType.POOL, 60.0),
            createRideEntity("ride3", RideStatus.STARTED, RideType.LUXURY, 200.0),
            createRideEntity("ride4", RideStatus.COMPLETED, RideType.STANDARD, 120.0)
        );
    }
    
    @Test
    void testGetDashboardAnalytics() {
        when(rideRepository.findAll()).thenReturn(sampleRides);
        when(driverRepository.findAll()).thenReturn(List.of());
        
        AnalyticsResponse analytics = analyticsService.getDashboardAnalytics();
        
        assertNotNull(analytics);
        assertEquals(4, analytics.getTotalRides());
        assertEquals(3, analytics.getCompletedRides());
        assertEquals(1, analytics.getActiveRides());
        assertEquals(280.0, analytics.getTotalRevenue(), 0.01);
        assertTrue(analytics.getAverageFare() > 0);
    }
    
    @Test
    void testGetDriverEarnings() {
        when(rideRepository.findByDriverId("driver1")).thenReturn(
            List.of(
                createRideEntity("ride1", RideStatus.COMPLETED, RideType.STANDARD, 100.0),
                createRideEntity("ride2", RideStatus.COMPLETED, RideType.STANDARD, 120.0)
            )
        );
        
        double earnings = analyticsService.getDriverEarnings("driver1");
        
        assertEquals(220.0, earnings, 0.01);
    }
    
    @Test
    void testGetFilteredRides() {
        when(rideRepository.findAll()).thenReturn(sampleRides);
        
        List<RideEntity> filtered = analyticsService.getFilteredRides(
            RideStatus.COMPLETED, 
            RideType.STANDARD
        );
        
        assertNotNull(filtered);
        assertEquals(2, filtered.size());
        assertTrue(filtered.stream().allMatch(r -> 
            r.getStatus() == RideStatus.COMPLETED && r.getRideType() == RideType.STANDARD
        ));
    }
    
    private RideEntity createRideEntity(String id, RideStatus status, RideType type, double fare) {
        return RideEntity.builder()
                .id(id)
                .riderId("rider1")
                .driverId("driver1")
                .status(status)
                .rideType(type)
                .fare(fare)
                .distance(10.0)
                .surgeMultiplier(1.0)
                .requestedAt(LocalDateTime.now())
                .build();
    }
}
