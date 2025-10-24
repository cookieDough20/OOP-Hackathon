package com.ridesync.api.controller;

import com.ridesync.api.dto.DriverRequest;
import com.ridesync.api.service.DriverService;
import com.ridesync.core.model.Driver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for driver management operations.
 */
@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@Tag(name = "Driver Management", description = "APIs for driver registration and management")
public class DriverController {
    
    private final DriverService driverService;
    
    @PostMapping
    @Operation(summary = "Register a new driver", description = "Register a driver with vehicle details")
    public ResponseEntity<Driver> registerDriver(@Valid @RequestBody DriverRequest request) {
        Driver driver = driverService.registerDriver(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(driver);
    }
    
    @GetMapping("/{driverId}")
    @Operation(summary = "Get driver by ID")
    public ResponseEntity<Driver> getDriver(@PathVariable String driverId) {
        Driver driver = driverService.getDriver(driverId);
        return ResponseEntity.ok(driver);
    }
    
    @GetMapping
    @Operation(summary = "Get all drivers")
    public ResponseEntity<List<Driver>> getAllDrivers() {
        List<Driver> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(drivers);
    }
    
    @GetMapping("/available")
    @Operation(summary = "Get available drivers")
    public ResponseEntity<List<Driver>> getAvailableDrivers() {
        List<Driver> drivers = driverService.getAvailableDrivers();
        return ResponseEntity.ok(drivers);
    }
    
    @PutMapping("/{driverId}/location")
    @Operation(summary = "Update driver location")
    public ResponseEntity<Void> updateLocation(
            @PathVariable String driverId,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(required = false) String address) {
        driverService.updateDriverLocation(driverId, latitude, longitude, address);
        return ResponseEntity.ok().build();
    }
}
