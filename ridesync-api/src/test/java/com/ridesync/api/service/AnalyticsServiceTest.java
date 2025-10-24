package com.ridesync.api.service;

import com.ridesync.core.model.enums.RideStatus;
import com.ridesync.core.model.enums.RideType;
import com.ridesync.persistence.entity.RideEntity;
import com.ridesync.persistence.repository.DriverRepository;
import com.ridesync.persistence.repository.RideRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Analytics Service Tests")
class AnalyticsServiceTest {
    
    @Mock
    private RideRepository rideRepository;
    
    @Mock
    private DriverRepository driverRepository;
    
    @InjectMocks
    private AnalyticsService analyticsService;
    
    @Test
    @DisplayName("Should calculate driver earnings correctly")
    void shouldCalculateDriverEarnings() {
        String driverId = "driver1";
        
        List<RideEntity> rides = Arrays.asList(
            createRideEntity("ride1", driverId, 100.0, RideStatus.COMPLETED),
            createRideEntity("ride2", driverId, 150.0, RideStatus.COMPLETED),
            createRideEntity("ride3", driverId, 80.0, RideStatus.CANCELLED)
        );
        
        when(rideRepository.findByDriverId(driverId)).thenReturn(rides);
        
        double earnings = analyticsService.calculateDriverEarnings(driverId);
        
        assertThat(earnings).isEqualTo(250.0);
    }
    
    @Test
    @DisplayName("Should group rides by type")
    void shouldGroupRidesByType() {
        List<RideEntity> rides = Arrays.asList(
            createRideEntityWithType("ride1", RideType.STANDARD),
            createRideEntityWithType("ride2", RideType.STANDARD),
            createRideEntityWithType("ride3", RideType.POOL)
        );
        
        when(rideRepository.findAll()).thenReturn(rides);
        
        var grouped = analyticsService.groupRidesByType();
        
        assertThat(grouped).containsKeys(RideType.STANDARD, RideType.POOL);
        assertThat(grouped.get(RideType.STANDARD)).hasSize(2);
        assertThat(grouped.get(RideType.POOL)).hasSize(1);
    }
    
    private RideEntity createRideEntity(String id, String driverId, double fare, RideStatus status) {
        return RideEntity.builder()
            .id(id)
            .riderId("rider1")
            .driverId(driverId)
            .rideType(RideType.STANDARD)
            .startLatitude(40.7128)
            .startLongitude(-74.0060)
            .endLatitude(40.7580)
            .endLongitude(-73.9855)
            .distance(5.0)
            .fare(fare)
            .status(status)
            .requestedAt(LocalDateTime.now())
            .build();
    }
    
    private RideEntity createRideEntityWithType(String id, RideType type) {
        return RideEntity.builder()
            .id(id)
            .riderId("rider1")
            .rideType(type)
            .startLatitude(40.7128)
            .startLongitude(-74.0060)
            .endLatitude(40.7580)
            .endLongitude(-73.9855)
            .distance(5.0)
            .fare(100.0)
            .status(RideStatus.COMPLETED)
            .requestedAt(LocalDateTime.now())
            .build();
    }
}
