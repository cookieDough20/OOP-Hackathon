package com.ridesync.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Response DTO for analytics data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    private long totalRides;
    private long completedRides;
    private long activeRides;
    private double totalRevenue;
    private double averageFare;
    private double averageDistance;
    private Map<String, Long> ridesByType;
    private Map<String, Long> ridesByStatus;
    private java.util.List<TopDriverDTO> topDrivers;
}
