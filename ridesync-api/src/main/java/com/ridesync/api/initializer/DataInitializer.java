package com.ridesync.api.initializer;

import com.ridesync.core.model.Driver;
import com.ridesync.core.model.Location;
import com.ridesync.core.model.Rider;
import com.ridesync.core.model.enums.VehicleType;
import com.ridesync.persistence.mapper.DriverMapper;
import com.ridesync.persistence.mapper.RiderMapper;
import com.ridesync.persistence.repository.DriverRepository;
import com.ridesync.persistence.repository.RiderRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Data initializer to seed sample data on startup
 * Creates 5 drivers and 10 riders as per requirements
 */
@Component
@RequiredArgsConstructor
public class DataInitializer {
    
    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;
    
    @PostConstruct
    public void init() {
        System.out.println("Initializing sample data...");
        
        // Seed 5 drivers
        seedDrivers();
        
        // Seed 10 riders
        seedRiders();
        
        System.out.println("Sample data initialized successfully!");
        System.out.println("- " + driverRepository.count() + " drivers registered");
        System.out.println("- " + riderRepository.count() + " riders registered");
    }
    
    private void seedDrivers() {
        Driver[] drivers = {
            Driver.builder()
                .name("John Smith")
                .phoneNumber("+1-555-0101")
                .vehicleType(VehicleType.SEDAN)
                .licensePlate("ABC-1234")
                .currentLocation(new Location(40.7128, -74.0060)) // NYC
                .build(),
            
            Driver.builder()
                .name("Sarah Johnson")
                .phoneNumber("+1-555-0102")
                .vehicleType(VehicleType.SUV)
                .licensePlate("XYZ-5678")
                .currentLocation(new Location(40.7580, -73.9855)) // NYC
                .build(),
            
            Driver.builder()
                .name("Michael Chen")
                .phoneNumber("+1-555-0103")
                .vehicleType(VehicleType.LUXURY)
                .licensePlate("LUX-9999")
                .currentLocation(new Location(40.7489, -73.9680)) // NYC
                .build(),
            
            Driver.builder()
                .name("Emily Davis")
                .phoneNumber("+1-555-0104")
                .vehicleType(VehicleType.MINI)
                .licensePlate("MINI-1111")
                .currentLocation(new Location(40.7614, -73.9776)) // NYC
                .build(),
            
            Driver.builder()
                .name("Robert Garcia")
                .phoneNumber("+1-555-0105")
                .vehicleType(VehicleType.SEDAN)
                .licensePlate("STD-2468")
                .currentLocation(new Location(40.7589, -73.9851)) // NYC
                .build()
        };
        
        for (Driver driver : drivers) {
            driverRepository.save(DriverMapper.toEntity(driver));
        }
    }
    
    private void seedRiders() {
        Rider[] riders = {
            Rider.builder()
                .name("Alice Williams")
                .phoneNumber("+1-555-1001")
                .email("alice@example.com")
                .currentLocation(new Location(40.7128, -74.0060))
                .build(),
            
            Rider.builder()
                .name("Bob Brown")
                .phoneNumber("+1-555-1002")
                .email("bob@example.com")
                .currentLocation(new Location(40.7580, -73.9855))
                .build(),
            
            Rider.builder()
                .name("Carol Martinez")
                .phoneNumber("+1-555-1003")
                .email("carol@example.com")
                .currentLocation(new Location(40.7489, -73.9680))
                .build(),
            
            Rider.builder()
                .name("David Lee")
                .phoneNumber("+1-555-1004")
                .email("david@example.com")
                .currentLocation(new Location(40.7614, -73.9776))
                .build(),
            
            Rider.builder()
                .name("Eva Taylor")
                .phoneNumber("+1-555-1005")
                .email("eva@example.com")
                .currentLocation(new Location(40.7589, -73.9851))
                .build(),
            
            Rider.builder()
                .name("Frank Anderson")
                .phoneNumber("+1-555-1006")
                .email("frank@example.com")
                .currentLocation(new Location(40.7306, -73.9352))
                .build(),
            
            Rider.builder()
                .name("Grace Wilson")
                .phoneNumber("+1-555-1007")
                .email("grace@example.com")
                .currentLocation(new Location(40.7484, -73.9857))
                .build(),
            
            Rider.builder()
                .name("Henry Moore")
                .phoneNumber("+1-555-1008")
                .email("henry@example.com")
                .currentLocation(new Location(40.7549, -73.9840))
                .build(),
            
            Rider.builder()
                .name("Ivy Jackson")
                .phoneNumber("+1-555-1009")
                .email("ivy@example.com")
                .currentLocation(new Location(40.7505, -73.9934))
                .build(),
            
            Rider.builder()
                .name("Jack White")
                .phoneNumber("+1-555-1010")
                .email("jack@example.com")
                .currentLocation(new Location(40.7580, -73.9910))
                .build()
        };
        
        for (Rider rider : riders) {
            riderRepository.save(RiderMapper.toEntity(rider));
        }
    }
}
