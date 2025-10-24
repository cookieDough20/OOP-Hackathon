package com.ridesync.api.dto;

import com.ridesync.core.model.enums.RideStatus;
import com.ridesync.core.model.enums.RideType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for ride response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideResponse {
    private String rideId;
    private String riderId;
    private String driverId;
    private String driverName;
    private String driverPhone;
    private String vehicleInfo;
    private RideType rideType;
    private RideStatus status;
    private double distance;
    private double estimatedFare;
    private double actualFare;
    private double surgeMultiplier;
    private LocalDateTime requestedAt;
    private LocalDateTime estimatedArrival;
    private String message;
}
