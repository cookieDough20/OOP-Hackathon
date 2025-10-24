package com.ridesync.api.service;

import com.ridesync.core.model.Ride;
import com.ridesync.core.model.enums.RideStatus;
import com.ridesync.core.model.enums.RideType;
import com.ridesync.persistence.mapper.DriverMapper;
import com.ridesync.persistence.mapper.RideMapper;
import com.ridesync.persistence.repository.DriverRepository;
import com.ridesync.persistence.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Analytics service using Java Streams for data analytics
 * WOW Factor: Demonstrates advanced stream operations
 */
@Service
@RequiredArgsConstructor
public class AnalyticsService {
    
    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    
    /**
     * Calculate driver earnings using Java Streams
     */
    public double calculateDriverEarnings(String driverId) {
        return rideRepository.findByDriverId(driverId)
            .stream()
            .filter(ride -> ride.getStatus() == RideStatus.COMPLETED)
            .mapToDouble(ride -> ride.getFare())
            .sum();
    }
    
    /**
     * Get rides filtered by status and/or type using Streams
     */
    public List<Ride> getFilteredRides(RideStatus status, RideType type) {
        return rideRepository.findAll()
            .stream()
            .map(RideMapper::toDomain)
            .filter(ride -> status == null || ride.getStatus() == status)
            .filter(ride -> type == null || ride.getRideType() == type)
            .collect(Collectors.toList());
    }
    
    /**
     * Group rides by type using Collectors.groupingBy
     */
    public Map<RideType, List<Ride>> groupRidesByType() {
        return rideRepository.findAll()
            .stream()
            .map(RideMapper::toDomain)
            .collect(Collectors.groupingBy(Ride::getRideType));
    }
    
    /**
     * Group rides by status
     */
    public Map<RideStatus, List<Ride>> groupRidesByStatus() {
        return rideRepository.findAll()
            .stream()
            .map(RideMapper::toDomain)
            .collect(Collectors.groupingBy(Ride::getStatus));
    }
    
    /**
     * Calculate average fare by ride type
     */
    public Map<RideType, Double> getAverageFareByType() {
        return rideRepository.findAll()
            .stream()
            .filter(ride -> ride.getStatus() == RideStatus.COMPLETED)
            .collect(Collectors.groupingBy(
                ride -> ride.getRideType(),
                Collectors.averagingDouble(ride -> ride.getFare())
            ));
    }
    
    /**
     * Get top earning drivers using Streams
     */
    public List<Map<String, Object>> getTopEarningDrivers(int limit) {
        return driverRepository.findAll()
            .stream()
            .map(DriverMapper::toDomain)
            .sorted(Comparator.comparingDouble(driver -> -driver.getEarnings()))
            .limit(limit)
            .map(driver -> {
                Map<String, Object> driverStats = new HashMap<>();
                driverStats.put("driverId", driver.getId());
                driverStats.put("name", driver.getName());
                driverStats.put("earnings", driver.getEarnings());
                driverStats.put("totalRides", driver.getTotalRides());
                driverStats.put("rating", driver.getRating());
                return driverStats;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Get comprehensive dashboard statistics
     */
    public Map<String, Object> getDashboardStatistics() {
        List<Ride> allRides = rideRepository.findAll()
            .stream()
            .map(RideMapper::toDomain)
            .collect(Collectors.toList());
        
        long totalRides = allRides.size();
        long completedRides = allRides.stream()
            .filter(ride -> ride.getStatus() == RideStatus.COMPLETED)
            .count();
        
        double totalRevenue = allRides.stream()
            .filter(ride -> ride.getStatus() == RideStatus.COMPLETED)
            .mapToDouble(Ride::getFare)
            .sum();
        
        double avgFare = allRides.stream()
            .filter(ride -> ride.getStatus() == RideStatus.COMPLETED)
            .mapToDouble(Ride::getFare)
            .average()
            .orElse(0.0);
        
        long activeRides = allRides.stream()
            .filter(ride -> ride.getStatus() == RideStatus.IN_PROGRESS || 
                           ride.getStatus() == RideStatus.DRIVER_ASSIGNED)
            .count();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRides", totalRides);
        stats.put("completedRides", completedRides);
        stats.put("activeRides", activeRides);
        stats.put("totalRevenue", Math.round(totalRevenue * 100.0) / 100.0);
        stats.put("averageFare", Math.round(avgFare * 100.0) / 100.0);
        stats.put("totalDrivers", driverRepository.count());
        stats.put("availableDrivers", driverRepository.findAvailableDrivers().size());
        stats.put("topDrivers", getTopEarningDrivers(5));
        stats.put("ridesByType", groupRidesByType().entrySet().stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey().name(),
                entry -> entry.getValue().size()
            )));
        stats.put("ridesByStatus", groupRidesByStatus().entrySet().stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey().name(),
                entry -> entry.getValue().size()
            )));
        
        return stats;
    }
}
