package com.ridesync.api.controller;

import com.ridesync.api.dto.RegisterRiderRequest;
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
 * REST controller for rider operations
 */
@RestController
@RequestMapping("/api/riders")
@RequiredArgsConstructor
@Tag(name = "Riders", description = "Rider registration and management APIs")
public class RiderController {
    
    private final RiderService riderService;
    
    @PostMapping
    @Operation(summary = "Register a new rider", 
               description = "Register a rider to start booking rides")
    public ResponseEntity<Rider> registerRider(@Valid @RequestBody RegisterRiderRequest request) {
        Rider rider = riderService.registerRider(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(rider);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get rider by ID")
    public ResponseEntity<Rider> getRider(@PathVariable String id) {
        Rider rider = riderService.getRider(id);
        return ResponseEntity.ok(rider);
    }
    
    @GetMapping
    @Operation(summary = "Get all riders")
    public ResponseEntity<List<Rider>> getAllRiders() {
        List<Rider> riders = riderService.getAllRiders();
        return ResponseEntity.ok(riders);
    }
}
