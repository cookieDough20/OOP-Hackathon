package com.ridesync.persistence.entity;

import com.ridesync.core.model.RideStatus;
import com.ridesync.core.model.RideType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * JPA Entity for Ride persistence in H2 database.
 */
@Entity
@Table(name = "rides")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideEntity {
    
    @Id
    private String id;
    
    @Column(nullable = false)
    private String riderId;
    
    private String driverId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RideType rideType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RideStatus status;
    
    private double startLatitude;
    private double startLongitude;
    private String startAddress;
    
    private double endLatitude;
    private double endLongitude;
    private String endAddress;
    
    @Column(nullable = false)
    private double distance;
    
    @Column(nullable = false)
    private double fare;
    
    @Column(nullable = false)
    private double surgeMultiplier;
    
    @Column(nullable = false)
    private LocalDateTime requestedAt;
    
    private LocalDateTime startedAt;
    
    private LocalDateTime completedAt;
}
