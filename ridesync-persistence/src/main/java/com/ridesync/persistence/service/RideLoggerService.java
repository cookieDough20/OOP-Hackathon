package com.ridesync.persistence.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ridesync.core.model.Ride;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for file I/O operations - persisting ride logs to JSON files
 * Demonstrates file serialization as per requirements
 */
@Service
public class RideLoggerService {
    
    private static final String LOGS_DIRECTORY = "ride-logs";
    private static final String LOG_FILE_NAME = "rides.json";
    private final ObjectMapper objectMapper;
    
    public RideLoggerService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Create logs directory if it doesn't exist
        createLogsDirectory();
    }
    
    /**
     * Create logs directory
     */
    private void createLogsDirectory() {
        try {
            Path logsPath = Paths.get(LOGS_DIRECTORY);
            if (!Files.exists(logsPath)) {
                Files.createDirectories(logsPath);
            }
        } catch (IOException e) {
            System.err.println("Failed to create logs directory: " + e.getMessage());
        }
    }
    
    /**
     * Serialize and write a ride to JSON file
     * Demonstrates file I/O and JSON serialization
     */
    public void logRide(Ride ride) {
        try {
            Path logFile = Paths.get(LOGS_DIRECTORY, LOG_FILE_NAME);
            
            // Read existing rides
            List<Ride> rides = readAllRides();
            
            // Add new ride
            rides.add(ride);
            
            // Write back to file
            objectMapper.writeValue(logFile.toFile(), rides);
            
            // Also write individual ride log with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String individualLogName = String.format("ride_%s_%s.json", ride.getId(), timestamp);
            Path individualLog = Paths.get(LOGS_DIRECTORY, individualLogName);
            objectMapper.writeValue(individualLog.toFile(), ride);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to log ride: " + e.getMessage(), e);
        }
    }
    
    /**
     * Read all rides from the log file
     */
    public List<Ride> readAllRides() {
        try {
            Path logFile = Paths.get(LOGS_DIRECTORY, LOG_FILE_NAME);
            
            if (!Files.exists(logFile)) {
                return new ArrayList<>();
            }
            
            return objectMapper.readValue(
                logFile.toFile(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Ride.class)
            );
            
        } catch (IOException e) {
            System.err.println("Failed to read rides from log: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Read a specific ride log file
     */
    public Ride readRideById(String rideId) throws IOException {
        Path logFile = Paths.get(LOGS_DIRECTORY);
        
        return Files.list(logFile)
            .filter(path -> path.getFileName().toString().startsWith("ride_" + rideId))
            .findFirst()
            .map(path -> {
                try {
                    return objectMapper.readValue(path.toFile(), Ride.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .orElse(null);
    }
    
    /**
     * Get statistics from logs
     */
    public String getLogStatistics() {
        List<Ride> rides = readAllRides();
        return String.format("Total rides logged: %d", rides.size());
    }
}
