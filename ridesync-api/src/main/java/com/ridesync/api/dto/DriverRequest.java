package com.ridesync.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for driver registration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Vehicle is required")
    private String vehicle;
    
    @NotBlank(message = "Vehicle number is required")
    private String vehicleNumber;
    
    @NotNull(message = "Latitude is required")
    private Double latitude;
    
    @NotNull(message = "Longitude is required")
    private Double longitude;
    
    private String address;
}
