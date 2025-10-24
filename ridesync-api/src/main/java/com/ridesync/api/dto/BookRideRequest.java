package com.ridesync.api.dto;

import com.ridesync.core.model.enums.RideType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for ride booking request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRideRequest {
    
    @NotBlank(message = "Rider ID is required")
    private String riderId;
    
    @NotNull(message = "Start latitude is required")
    @Min(value = -90, message = "Latitude must be between -90 and 90")
    @Max(value = 90, message = "Latitude must be between -90 and 90")
    private Double startLatitude;
    
    @NotNull(message = "Start longitude is required")
    @Min(value = -180, message = "Longitude must be between -180 and 180")
    @Max(value = 180, message = "Longitude must be between -180 and 180")
    private Double startLongitude;
    
    @NotNull(message = "End latitude is required")
    @Min(value = -90, message = "Latitude must be between -90 and 90")
    @Max(value = 90, message = "Latitude must be between -90 and 90")
    private Double endLatitude;
    
    @NotNull(message = "End longitude is required")
    @Min(value = -180, message = "Longitude must be between -180 and 180")
    @Max(value = 180, message = "Longitude must be between -180 and 180")
    private Double endLongitude;
    
    @NotNull(message = "Ride type is required")
    private RideType rideType;
}
