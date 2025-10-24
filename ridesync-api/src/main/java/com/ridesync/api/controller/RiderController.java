package com.ridesync.api.controller;

import com.ridesync.api.dto.RiderRequest;
import com.ridesync.api.service.RiderService;
import com.ridesync.core.model.Rider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for rider management operations.
 */
@RestController
@RequestMapping("/api/riders")
@RequiredArgsConstructor
@Tag(name = "Rider Management", description = "APIs for rider registration and management")
public class RiderController {
    
    private final RiderService riderService;
    
    @PostMapping
    @Operation(summary = "Register a new rider", description = "Register a rider with contact details")
    public ResponseEntity<Rider> registerRider(@Valid @RequestBody RiderRequest request) {
        Rider rider = riderService.registerRider(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(rider);
    }
    
    @GetMapping("/{riderId}")
    @Operation(summary = "Get rider by ID")
    public ResponseEntity<Rider> getRider(@PathVariable String riderId) {
        Rider rider = riderService.getRider(riderId);
        return ResponseEntity.ok(rider);
    }
    
    @GetMapping
    @Operation(summary = "Get all riders")
    public ResponseEntity<List<Rider>> getAllRiders() {
        List<Rider> riders = riderService.getAllRiders();
        return ResponseEntity.ok(riders);
    }
}
