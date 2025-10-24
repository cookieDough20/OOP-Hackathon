package com.ridesync.persistence.entity;

import com.ridesync.core.model.DriverStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA Entity for Driver persistence in H2 database.
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
    private String vehicle;
    
    @Column(nullable = false)
    private String vehicleNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverStatus status;
    
    private double currentLatitude;
    private double currentLongitude;
    private String currentAddress;
    
    @Column(nullable = false)
    private double totalEarnings;
    
    @Column(nullable = false)
    private double rating;
}
