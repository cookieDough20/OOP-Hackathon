package com.ridesync.api.dto;

import com.ridesync.core.model.RideStatus;
import com.ridesync.core.model.RideType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for ride information.
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
    private String driverVehicle;
    private RideType rideType;
    private RideStatus status;
    private double distance;
    private double estimatedFare;
    private double actualFare;
    private double surgeMultiplier;
    private LocalDateTime requestedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String message;
}
