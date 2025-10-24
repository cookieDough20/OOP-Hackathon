package com.ridesync.api.dto;

import com.ridesync.core.model.RideType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for booking a ride.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRideRequest {
    
    @NotBlank(message = "Rider ID is required")
    private String riderId;
    
    @NotNull(message = "Start latitude is required")
    private Double startLatitude;
    
    @NotNull(message = "Start longitude is required")
    private Double startLongitude;
    
    private String startAddress;
    
    @NotNull(message = "End latitude is required")
    private Double endLatitude;
    
    @NotNull(message = "End longitude is required")
    private Double endLongitude;
    
    private String endAddress;
    
    @NotNull(message = "Ride type is required")
    private RideType rideType;
}
