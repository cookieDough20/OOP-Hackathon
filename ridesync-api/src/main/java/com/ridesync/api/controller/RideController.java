package com.ridesync.api.controller;

import com.ridesync.api.dto.BookRideRequest;
import com.ridesync.api.dto.RideResponse;
import com.ridesync.api.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for ride operations.
 */
@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
@Tag(name = "Ride Operations", description = "APIs for booking, tracking, and managing rides")
public class RideController {
    
    private final RideService rideService;
    
    @PostMapping("/book")
    @Operation(summary = "Book a new ride", description = "Book a ride with automatic driver assignment")
    public ResponseEntity<RideResponse> bookRide(@Valid @RequestBody BookRideRequest request) {
        RideResponse response = rideService.bookRide(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{rideId}")
    @Operation(summary = "Get ride details", description = "Track ride status and details")
    public ResponseEntity<RideResponse> getRide(@PathVariable String rideId) {
        RideResponse response = rideService.getRide(rideId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{rideId}/start")
    @Operation(summary = "Start a ride", description = "Driver starts the ride")
    public ResponseEntity<RideResponse> startRide(@PathVariable String rideId) {
        RideResponse response = rideService.startRide(rideId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{rideId}/complete")
    @Operation(summary = "Complete a ride", description = "Complete ride and calculate earnings")
    public ResponseEntity<RideResponse> completeRide(@PathVariable String rideId) {
        RideResponse response = rideService.completeRide(rideId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/rider/{riderId}")
    @Operation(summary = "Get rides for a rider")
    public ResponseEntity<List<RideResponse>> getRiderRides(@PathVariable String riderId) {
        List<RideResponse> rides = rideService.getRiderRides(riderId);
        return ResponseEntity.ok(rides);
    }
    
    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get rides for a driver")
    public ResponseEntity<List<RideResponse>> getDriverRides(@PathVariable String driverId) {
        List<RideResponse> rides = rideService.getDriverRides(driverId);
        return ResponseEntity.ok(rides);
    }
}
