package com.ridesync.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Rider entity representing a passenger in the ride-sharing platform
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rider {
    
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    
    @NotBlank(message = "Rider name is required")
    private String name;
    
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    
    @Email(message = "Valid email is required")
    private String email;
    
    @NotNull(message = "Current location is required")
    private Location currentLocation;
    
    @Builder.Default
    private double walletBalance = 1000.0; // Starting balance for demo
    
    @Builder.Default
    private double rating = 5.0;
    
    @JsonIgnore
    @Builder.Default
    private List<Ride> rideHistory = new ArrayList<>();
    
    /**
     * Add a ride to rider's history
     */
    public void addRide(Ride ride) {
        this.rideHistory.add(ride);
    }
    
    /**
     * Deduct fare from wallet
     */
    public void deductFare(double fare) {
        this.walletBalance -= fare;
    }
    
    /**
     * Check if rider has sufficient balance
     */
    public boolean hasSufficientBalance(double fare) {
        return this.walletBalance >= fare;
    }
}
