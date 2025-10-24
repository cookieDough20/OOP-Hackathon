package com.ridesync.persistence.entity;

import com.ridesync.core.model.enums.DriverStatus;
import com.ridesync.core.model.enums.VehicleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA Entity for Driver persistence in H2 database
 */
@Entity
@Table(name = "drivers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverEntity {
    
    @Id
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;
    
    @Column(nullable = false)
    private String licensePlate;
    
    @Column(nullable = false)
    private double currentLatitude;
    
    @Column(nullable = false)
    private double currentLongitude;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverStatus status;
    
    @Column(nullable = false)
    private double earnings;
    
    @Column(nullable = false)
    private double rating;
    
    @Column(nullable = false)
    private int totalRides;
}
