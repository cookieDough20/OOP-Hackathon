package com.ridesync.api.controller;

import com.ridesync.api.service.AnalyticsService;
import com.ridesync.core.model.Ride;
import com.ridesync.core.model.enums.RideStatus;
import com.ridesync.core.model.enums.RideType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for analytics and reporting
 * WOW Factor: Stream-based data analytics
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Data analytics and reporting APIs")
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    
    @GetMapping("/earnings/{driverId}")
    @Operation(summary = "Get driver earnings", 
               description = "Calculate total earnings using Java Streams")
    public ResponseEntity<Map<String, Object>> getDriverEarnings(@PathVariable String driverId) {
        double earnings = analyticsService.calculateDriverEarnings(driverId);
        return ResponseEntity.ok(Map.of(
            "driverId", driverId,
            "totalEarnings", earnings
        ));
    }
    
    @GetMapping("/rides")
    @Operation(summary = "Get filtered rides", 
               description = "Filter rides by status and/or type using Stream API")
    public ResponseEntity<List<Ride>> getFilteredRides(
            @RequestParam(required = false) RideStatus status,
            @RequestParam(required = false) RideType type) {
        List<Ride> rides = analyticsService.getFilteredRides(status, type);
        return ResponseEntity.ok(rides);
    }
    
    @GetMapping("/rides/by-type")
    @Operation(summary = "Group rides by type", 
               description = "Demonstrates Collectors.groupingBy from Stream API")
    public ResponseEntity<Map<RideType, List<Ride>>> getRidesByType() {
        Map<RideType, List<Ride>> grouped = analyticsService.groupRidesByType();
        return ResponseEntity.ok(grouped);
    }
    
    @GetMapping("/rides/by-status")
    @Operation(summary = "Group rides by status")
    public ResponseEntity<Map<RideStatus, List<Ride>>> getRidesByStatus() {
        Map<RideStatus, List<Ride>> grouped = analyticsService.groupRidesByStatus();
        return ResponseEntity.ok(grouped);
    }
    
    @GetMapping("/average-fare")
    @Operation(summary = "Get average fare by ride type")
    public ResponseEntity<Map<RideType, Double>> getAverageFareByType() {
        Map<RideType, Double> avgFares = analyticsService.getAverageFareByType();
        return ResponseEntity.ok(avgFares);
    }
    
    @GetMapping("/top-drivers")
    @Operation(summary = "Get top earning drivers", 
               description = "Sorted using Stream API")
    public ResponseEntity<List<Map<String, Object>>> getTopDrivers(
            @RequestParam(defaultValue = "10") int limit) {
        List<Map<String, Object>> topDrivers = analyticsService.getTopEarningDrivers(limit);
        return ResponseEntity.ok(topDrivers);
    }
}
