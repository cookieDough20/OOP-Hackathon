package com.ridesync.api.dto;

import com.ridesync.core.model.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for driver registration
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDriverRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    
    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;
    
    @NotBlank(message = "License plate is required")
    private String licensePlate;
    
    @NotNull(message = "Current latitude is required")
    private Double currentLatitude;
    
    @NotNull(message = "Current longitude is required")
    private Double currentLongitude;
}
