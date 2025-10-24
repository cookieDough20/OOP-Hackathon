package com.ridesync.api.controller;

import com.ridesync.api.dto.RegisterDriverRequest;
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
 * REST controller for driver operations
 */
@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@Tag(name = "Drivers", description = "Driver registration and management APIs")
public class DriverController {
    
    private final DriverService driverService;
    
    @PostMapping
    @Operation(summary = "Register a new driver", 
               description = "Register a driver with vehicle details")
    public ResponseEntity<Driver> registerDriver(@Valid @RequestBody RegisterDriverRequest request) {
        Driver driver = driverService.registerDriver(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(driver);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get driver by ID")
    public ResponseEntity<Driver> getDriver(@PathVariable String id) {
        Driver driver = driverService.getDriver(id);
        return ResponseEntity.ok(driver);
    }
    
    @GetMapping
    @Operation(summary = "Get all drivers")
    public ResponseEntity<List<Driver>> getAllDrivers() {
        List<Driver> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(drivers);
    }
    
    @GetMapping("/available")
    @Operation(summary = "Get available drivers", 
               description = "Get all drivers currently available for rides")
    public ResponseEntity<List<Driver>> getAvailableDrivers() {
        List<Driver> drivers = driverService.getAvailableDrivers();
        return ResponseEntity.ok(drivers);
    }
}
