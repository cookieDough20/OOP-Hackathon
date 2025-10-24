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

/**
 * REST controller for ride operations
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Rides", description = "Ride booking and management APIs")
public class RideController {
    
    private final RideService rideService;
    
    @PostMapping("/bookRide")
    @Operation(summary = "Book a new ride", 
               description = "Book a ride with dynamic pricing and automatic driver allocation")
    public ResponseEntity<RideResponse> bookRide(@Valid @RequestBody BookRideRequest request) {
        RideResponse response = rideService.bookRide(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/rides/{rideId}")
    @Operation(summary = "Get ride details", 
               description = "Track ride status and get real-time updates")
    public ResponseEntity<RideResponse> getRide(@PathVariable String rideId) {
        RideResponse response = rideService.getRide(rideId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/startRide/{rideId}")
    @Operation(summary = "Start a ride", 
               description = "Driver starts the ride when passenger boards")
    public ResponseEntity<RideResponse> startRide(@PathVariable String rideId) {
        RideResponse response = rideService.startRide(rideId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/completeRide/{rideId}")
    @Operation(summary = "Complete a ride", 
               description = "Complete ride and calculate final earnings")
    public ResponseEntity<RideResponse> completeRide(@PathVariable String rideId) {
        RideResponse response = rideService.completeRide(rideId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/cancelRide/{rideId}")
    @Operation(summary = "Cancel a ride", 
               description = "Cancel a ride and free up the driver")
    public ResponseEntity<RideResponse> cancelRide(@PathVariable String rideId) {
        RideResponse response = rideService.cancelRide(rideId);
        return ResponseEntity.ok(response);
    }
}
