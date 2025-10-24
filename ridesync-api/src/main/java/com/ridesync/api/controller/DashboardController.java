package com.ridesync.api.controller;

import com.ridesync.api.service.AnalyticsService;
import com.ridesync.core.util.ReflectionUtil;
import com.ridesync.core.model.Ride;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Dashboard controller with WOW features
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard and system information APIs")
public class DashboardController {
    
    private final AnalyticsService analyticsService;
    
    @GetMapping
    @Operation(summary = "Get dashboard statistics", 
               description = "Comprehensive platform statistics using Stream analytics")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> stats = analyticsService.getDashboardStatistics();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/reflection/ride")
    @Operation(summary = "Get Ride class metadata", 
               description = "Demonstrates reflection capabilities for debugging")
    public ResponseEntity<Map<String, Object>> getRideClassMetadata() {
        Map<String, Object> metadata = ReflectionUtil.getClassMetadata(Ride.class);
        return ResponseEntity.ok(metadata);
    }
    
    @GetMapping("/flowchart")
    @Operation(summary = "Get system flowchart", 
               description = "ASCII art representation of ride booking flow")
    public ResponseEntity<String> getFlowchart() {
        String flowchart = """
            ╔═══════════════════════════════════════════════════════════════╗
            ║              RideSync Solutions - Booking Flow                ║
            ╚═══════════════════════════════════════════════════════════════╝
            
                        [1] Rider Requests Ride
                                   ↓
                        [2] Factory Creates Ride Object
                         (StandardRide/PoolRide/LuxuryRide)
                                   ↓
                        [3] Surge Pricing Calculation
                         (Time + Location + Demand)
                                   ↓
                        [4] Strategy Pattern: Calculate Fare
                         (StandardFareStrategy/PoolFareStrategy/LuxuryFareStrategy)
                                   ↓
                        [5] Validate Rider Balance
                                   ↓
                        [6] Singleton Allocator: Find Driver
                         (Thread-safe with ReentrantLock)
                                   ↓
                        [7] Assign Driver to Ride
                                   ↓
                        [8] Persist to H2 Database (JPA)
                                   ↓
                        [9] Log to JSON File (File I/O)
                                   ↓
                        [10] WebSocket: Real-time Notification
                                   ↓
                        [11] Return Ride Details to Client
            
            ╔═══════════════════════════════════════════════════════════════╗
            ║  Design Patterns: Factory, Strategy, Singleton               ║
            ║  Concurrency: ReentrantLock, Synchronized Methods            ║
            ║  Data Processing: Java Streams, Collectors                   ║
            ║  Persistence: JPA (H2) + File I/O (JSON)                     ║
            ╚═══════════════════════════════════════════════════════════════╝
            """;
        
        return ResponseEntity.ok(flowchart);
    }
    
    @GetMapping("/health")
    @Operation(summary = "System health check")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "RideSync Solutions",
            "version", "1.0.0",
            "features", Map.of(
                "surgePricing", true,
                "webSocket", true,
                "analytics", true,
                "concurrency", true
            )
        ));
    }
}
