package com.ridesync.api.controller;

import com.ridesync.api.dto.AnalyticsResponse;
import com.ridesync.api.dto.TopDriverDTO;
import com.ridesync.api.service.AnalyticsService;
import com.ridesync.core.model.RideStatus;
import com.ridesync.core.model.RideType;
import com.ridesync.persistence.entity.RideEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for analytics and reporting.
 * Demonstrates advanced Java Streams usage.
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Analytics and reporting APIs with Stream operations")
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    
    @GetMapping("/dashboard")
    @Operation(summary = "Get dashboard analytics", 
               description = "Comprehensive analytics with Stream-based calculations")
    public ResponseEntity<AnalyticsResponse> getDashboard() {
        AnalyticsResponse analytics = analyticsService.getDashboardAnalytics();
        return ResponseEntity.ok(analytics);
    }
    
    @GetMapping("/earnings/{driverId}")
    @Operation(summary = "Get driver earnings", 
               description = "Calculate total earnings using Java Streams")
    public ResponseEntity<Map<String, Object>> getDriverEarnings(@PathVariable String driverId) {
        double earnings = analyticsService.getDriverEarnings(driverId);
        return ResponseEntity.ok(Map.of(
            "driverId", driverId,
            "totalEarnings", earnings
        ));
    }
    
    @GetMapping("/rides")
    @Operation(summary = "Get filtered rides", 
               description = "Filter rides by status and type using Streams")
    public ResponseEntity<List<RideEntity>> getFilteredRides(
            @RequestParam(required = false) RideStatus status,
            @RequestParam(required = false) RideType type) {
        List<RideEntity> rides = analyticsService.getFilteredRides(status, type);
        return ResponseEntity.ok(rides);
    }
    
    @GetMapping("/top-drivers")
    @Operation(summary = "Get top drivers", 
               description = "Top drivers by earnings with Stream sorting")
    public ResponseEntity<List<TopDriverDTO>> getTopDrivers(
            @RequestParam(defaultValue = "10") int limit) {
        List<TopDriverDTO> topDrivers = analyticsService.getTopDrivers(limit);
        return ResponseEntity.ok(topDrivers);
    }
    
    @GetMapping("/fare-by-type")
    @Operation(summary = "Get average fare by ride type", 
               description = "Stream grouping and averaging operations")
    public ResponseEntity<Map<RideType, Double>> getAverageFareByType() {
        Map<RideType, Double> avgFares = analyticsService.getAverageFareByType();
        return ResponseEntity.ok(avgFares);
    }
}
