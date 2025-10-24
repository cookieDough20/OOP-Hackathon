package com.ridesync.api.config;

import com.ridesync.api.dto.DriverRequest;
import com.ridesync.api.dto.RiderRequest;
import com.ridesync.api.service.DriverService;
import com.ridesync.api.service.RiderService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Initialize sample data on application startup.
 * Seeds database with drivers and riders for demo purposes.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {
    
    private final DriverService driverService;
    private final RiderService riderService;
    
    @PostConstruct
    public void initData() {
        log.info("Initializing sample data...");
        
        // Create sample drivers
        createSampleDrivers();
        
        // Create sample riders
        createSampleRiders();
        
        log.info("Sample data initialization completed!");
    }
    
    private void createSampleDrivers() {
        String[][] drivers = {
            {"Rajesh Kumar", "Toyota Innova", "KA-01-AB-1234", "12.9716", "77.5946", "MG Road, Bangalore"},
            {"Amit Sharma", "Honda City", "KA-02-CD-5678", "12.9352", "77.6245", "Whitefield, Bangalore"},
            {"Priya Singh", "Maruti Swift", "KA-03-EF-9012", "12.9141", "77.6411", "Koramangala, Bangalore"},
            {"Suresh Reddy", "Hyundai Verna", "KA-04-GH-3456", "13.0358", "77.5970", "Hebbal, Bangalore"},
            {"Lakshmi Devi", "Ford EcoSport", "KA-05-IJ-7890", "12.9698", "77.7500", "Marathahalli, Bangalore"}
        };
        
        for (String[] driver : drivers) {
            try {
                DriverRequest request = DriverRequest.builder()
                        .name(driver[0])
                        .vehicle(driver[1])
                        .vehicleNumber(driver[2])
                        .latitude(Double.parseDouble(driver[3]))
                        .longitude(Double.parseDouble(driver[4]))
                        .address(driver[5])
                        .build();
                
                driverService.registerDriver(request);
                log.info("Created driver: {}", driver[0]);
            } catch (Exception e) {
                log.error("Failed to create driver: {}", driver[0], e);
            }
        }
    }
    
    private void createSampleRiders() {
        String[][] riders = {
            {"Ananya Patel", "9876543210", "12.9716", "77.5946", "Indiranagar, Bangalore"},
            {"Rohan Gupta", "9876543211", "12.9352", "77.6245", "HSR Layout, Bangalore"},
            {"Sneha Iyer", "9876543212", "12.9141", "77.6411", "BTM Layout, Bangalore"},
            {"Vikram Desai", "9876543213", "13.0358", "77.5970", "Yelahanka, Bangalore"},
            {"Meera Nair", "9876543214", "12.9698", "77.7500", "Bellandur, Bangalore"},
            {"Arjun Mehta", "9876543215", "12.9698", "77.6411", "Electronic City, Bangalore"},
            {"Divya Krishnan", "9876543216", "12.9141", "77.5946", "Jayanagar, Bangalore"},
            {"Karthik Rao", "9876543217", "13.0358", "77.6245", "Malleshwaram, Bangalore"},
            {"Pooja Shah", "9876543218", "12.9352", "77.7500", "Sarjapur, Bangalore"},
            {"Nikhil Joshi", "9876543219", "12.9716", "77.6411", "Richmond Town, Bangalore"}
        };
        
        for (String[] rider : riders) {
            try {
                RiderRequest request = RiderRequest.builder()
                        .name(rider[0])
                        .phone(rider[1])
                        .latitude(Double.parseDouble(rider[2]))
                        .longitude(Double.parseDouble(rider[3]))
                        .address(rider[4])
                        .build();
                
                riderService.registerRider(request);
                log.info("Created rider: {}", rider[0]);
            } catch (Exception e) {
                log.error("Failed to create rider: {}", rider[0], e);
            }
        }
    }
}
