package com.ridesync.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for top driver information in analytics.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopDriverDTO {
    private String driverId;
    private String driverName;
    private double totalEarnings;
    private long completedRides;
    private double rating;
}
