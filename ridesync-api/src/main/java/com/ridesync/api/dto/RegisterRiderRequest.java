package com.ridesync.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for rider registration
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRiderRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    private String email;
    
    @NotNull(message = "Current latitude is required")
    private Double currentLatitude;
    
    @NotNull(message = "Current longitude is required")
    private Double currentLongitude;
}
