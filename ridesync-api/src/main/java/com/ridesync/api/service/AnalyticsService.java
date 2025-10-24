package com.ridesync.api.service;

import com.ridesync.api.dto.AnalyticsResponse;
import com.ridesync.api.dto.TopDriverDTO;
import com.ridesync.core.model.RideStatus;
import com.ridesync.core.model.RideType;
import com.ridesync.persistence.entity.DriverEntity;
import com.ridesync.persistence.entity.RideEntity;
import com.ridesync.persistence.repository.DriverRepository;
import com.ridesync.persistence.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for analytics and reporting using Java Streams.
 * Implements advanced stream operations for data analysis.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsService {
    
    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    
    /**
     * Get comprehensive dashboard analytics.
     * Uses Java Streams for efficient data processing.
     */
    public AnalyticsResponse getDashboardAnalytics() {
        List<RideEntity> allRides = rideRepository.findAll();
        
        // Total rides count
        long totalRides = allRides.size();
        
        // Completed rides count
        long completedRides = allRides.stream()
                .filter(ride -> ride.getStatus() == RideStatus.COMPLETED)
                .count();
        
        // Active rides (REQUESTED, ASSIGNED, STARTED)
        long activeRides = allRides.stream()
                .filter(ride -> ride.getStatus() == RideStatus.REQUESTED ||
                               ride.getStatus() == RideStatus.ASSIGNED ||
                               ride.getStatus() == RideStatus.STARTED)
                .count();
        
        // Total revenue from completed rides
        double totalRevenue = allRides.stream()
                .filter(ride -> ride.getStatus() == RideStatus.COMPLETED)
                .mapToDouble(RideEntity::getFare)
                .sum();
        
        // Average fare
        double averageFare = allRides.stream()
                .filter(ride -> ride.getStatus() == RideStatus.COMPLETED)
                .mapToDouble(RideEntity::getFare)
                .average()
                .orElse(0.0);
        
        // Average distance
        double averageDistance = allRides.stream()
                .filter(ride -> ride.getStatus() == RideStatus.COMPLETED)
                .mapToDouble(RideEntity::getDistance)
                .average()
                .orElse(0.0);
        
        // Group rides by type
        Map<String, Long> ridesByType = allRides.stream()
                .collect(Collectors.groupingBy(
                    ride -> ride.getRideType().name(),
                    Collectors.counting()
                ));
        
        // Group rides by status
        Map<String, Long> ridesByStatus = allRides.stream()
                .collect(Collectors.groupingBy(
                    ride -> ride.getStatus().name(),
                    Collectors.counting()
                ));
        
        // Get top 5 drivers by earnings
        List<TopDriverDTO> topDrivers = getTopDrivers(5);
        
        return AnalyticsResponse.builder()
                .totalRides(totalRides)
                .completedRides(completedRides)
                .activeRides(activeRides)
                .totalRevenue(Math.round(totalRevenue * 100.0) / 100.0)
                .averageFare(Math.round(averageFare * 100.0) / 100.0)
                .averageDistance(Math.round(averageDistance * 100.0) / 100.0)
                .ridesByType(ridesByType)
                .ridesByStatus(ridesByStatus)
                .topDrivers(topDrivers)
                .build();
    }
    
    /**
     * Get driver earnings with stream-based calculation.
     */
    public double getDriverEarnings(String driverId) {
        List<RideEntity> driverRides = rideRepository.findByDriverId(driverId);
        
        return driverRides.stream()
                .filter(ride -> ride.getStatus() == RideStatus.COMPLETED)
                .mapToDouble(RideEntity::getFare)
                .sum();
    }
    
    /**
     * Get filtered rides by status and type.
     */
    public List<RideEntity> getFilteredRides(RideStatus status, RideType type) {
        List<RideEntity> rides = rideRepository.findAll();
        
        return rides.stream()
                .filter(ride -> (status == null || ride.getStatus() == status))
                .filter(ride -> (type == null || ride.getRideType() == type))
                .collect(Collectors.toList());
    }
    
    /**
     * Get top drivers by earnings.
     */
    public List<TopDriverDTO> getTopDrivers(int limit) {
        List<DriverEntity> drivers = driverRepository.findAll();
        List<RideEntity> allRides = rideRepository.findAll();
        
        // Group rides by driver
        Map<String, List<RideEntity>> ridesByDriver = allRides.stream()
                .filter(ride -> ride.getDriverId() != null)
                .collect(Collectors.groupingBy(RideEntity::getDriverId));
        
        return drivers.stream()
                .map(driver -> {
                    List<RideEntity> driverRides = ridesByDriver.getOrDefault(driver.getId(), List.of());
                    
                    double earnings = driverRides.stream()
                            .filter(ride -> ride.getStatus() == RideStatus.COMPLETED)
                            .mapToDouble(RideEntity::getFare)
                            .sum();
                    
                    long completedCount = driverRides.stream()
                            .filter(ride -> ride.getStatus() == RideStatus.COMPLETED)
                            .count();
                    
                    return TopDriverDTO.builder()
                            .driverId(driver.getId())
                            .driverName(driver.getName())
                            .totalEarnings(Math.round(earnings * 100.0) / 100.0)
                            .completedRides(completedCount)
                            .rating(driver.getRating())
                            .build();
                })
                .sorted(Comparator.comparingDouble(TopDriverDTO::getTotalEarnings).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Get ride statistics grouped by ride type.
     */
    public Map<RideType, Double> getAverageFareByType() {
        List<RideEntity> completedRides = rideRepository.findByStatus(RideStatus.COMPLETED);
        
        return completedRides.stream()
                .collect(Collectors.groupingBy(
                    RideEntity::getRideType,
                    Collectors.averagingDouble(RideEntity::getFare)
                ));
    }
}
