package com.ridesync.persistence.entity;

import com.ridesync.core.model.enums.RideStatus;
import com.ridesync.core.model.enums.RideType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * JPA Entity for Ride persistence in H2 database
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
    
    @Column
    private String driverId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RideType rideType;
    
    @Column(nullable = false)
    private double startLatitude;
    
    @Column(nullable = false)
    private double startLongitude;
    
    @Column(nullable = false)
    private double endLatitude;
    
    @Column(nullable = false)
    private double endLongitude;
    
    @Column(nullable = false)
    private double distance;
    
    @Column(nullable = false)
    private double fare;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RideStatus status;
    
    @Column(nullable = false)
    private LocalDateTime requestedAt;
    
    @Column
    private LocalDateTime startedAt;
    
    @Column
    private LocalDateTime completedAt;
}
