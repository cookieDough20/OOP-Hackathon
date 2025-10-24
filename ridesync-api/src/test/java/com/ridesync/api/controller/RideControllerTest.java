package com.ridesync.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridesync.api.dto.BookRideRequest;
import com.ridesync.api.dto.RideResponse;
import com.ridesync.api.service.RideService;
import com.ridesync.core.model.RideStatus;
import com.ridesync.core.model.RideType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for RideController REST APIs.
 */
@WebMvcTest(RideController.class)
class RideControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private RideService rideService;
    
    @Test
    void testBookRide() throws Exception {
        BookRideRequest request = BookRideRequest.builder()
                .riderId("rider123")
                .startLatitude(12.9716)
                .startLongitude(77.5946)
                .endLatitude(12.9698)
                .endLongitude(77.7500)
                .rideType(RideType.STANDARD)
                .build();
        
        RideResponse response = RideResponse.builder()
                .rideId("RIDE-12345678")
                .riderId("rider123")
                .driverId("driver123")
                .driverName("Test Driver")
                .rideType(RideType.STANDARD)
                .status(RideStatus.ASSIGNED)
                .distance(13.5)
                .estimatedFare(150.0)
                .surgeMultiplier(1.2)
                .requestedAt(LocalDateTime.now())
                .message("Ride booked successfully!")
                .build();
        
        when(rideService.bookRide(any(BookRideRequest.class))).thenReturn(response);
        
        mockMvc.perform(post("/api/rides/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rideId").value("RIDE-12345678"))
                .andExpect(jsonPath("$.status").value("ASSIGNED"))
                .andExpect(jsonPath("$.estimatedFare").value(150.0));
    }
    
    @Test
    void testGetRide() throws Exception {
        String rideId = "RIDE-12345678";
        
        RideResponse response = RideResponse.builder()
                .rideId(rideId)
                .riderId("rider123")
                .status(RideStatus.STARTED)
                .actualFare(150.0)
                .build();
        
        when(rideService.getRide(rideId)).thenReturn(response);
        
        mockMvc.perform(get("/api/rides/" + rideId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rideId").value(rideId))
                .andExpect(jsonPath("$.status").value("STARTED"));
    }
}
