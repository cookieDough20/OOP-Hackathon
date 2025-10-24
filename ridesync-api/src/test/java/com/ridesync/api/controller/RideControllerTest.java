package com.ridesync.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridesync.api.dto.BookRideRequest;
import com.ridesync.api.dto.RideResponse;
import com.ridesync.api.service.RideService;
import com.ridesync.core.model.enums.RideStatus;
import com.ridesync.core.model.enums.RideType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RideController.class, 
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class,
        org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
    })
@DisplayName("Ride Controller Integration Tests")
class RideControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private RideService rideService;
    
    @Test
    @DisplayName("POST /api/bookRide should book a ride successfully")
    void shouldBookRideSuccessfully() throws Exception {
        BookRideRequest request = BookRideRequest.builder()
            .riderId("rider1")
            .startLatitude(40.7128)
            .startLongitude(-74.0060)
            .endLatitude(40.7580)
            .endLongitude(-73.9855)
            .rideType(RideType.STANDARD)
            .build();
        
        RideResponse response = RideResponse.builder()
            .rideId("ride123")
            .riderId("rider1")
            .driverId("driver1")
            .status(RideStatus.DRIVER_ASSIGNED)
            .estimatedFare(100.0)
            .message("Ride booked successfully!")
            .build();
        
        when(rideService.bookRide(any(BookRideRequest.class))).thenReturn(response);
        
        mockMvc.perform(post("/api/bookRide")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.rideId").value("ride123"))
            .andExpect(jsonPath("$.status").value("DRIVER_ASSIGNED"));
    }
    
    @Test
    @DisplayName("GET /api/rides/{rideId} should return ride details")
    void shouldGetRideDetails() throws Exception {
        RideResponse response = RideResponse.builder()
            .rideId("ride123")
            .riderId("rider1")
            .status(RideStatus.IN_PROGRESS)
            .build();
        
        when(rideService.getRide(anyString())).thenReturn(response);
        
        mockMvc.perform(get("/api/rides/ride123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rideId").value("ride123"))
            .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }
}
