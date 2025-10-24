package com.ridesync.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA Entity for Rider persistence in H2 database
 */
@Entity
@Table(name = "riders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiderEntity {
    
    @Id
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private double currentLatitude;
    
    @Column(nullable = false)
    private double currentLongitude;
    
    @Column(nullable = false)
    private double walletBalance;
    
    @Column(nullable = false)
    private double rating;
}
