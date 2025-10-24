package com.ridesync.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ridesync.core.model.Ride;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for persisting ride data to JSON files.
 * Implements file I/O with JSON serialization using Jackson.
 */
@Slf4j
public class RideLogger {
    private static final String LOG_DIRECTORY = "ride-logs";
    private static final String LOG_FILE = "rides.json";
    private final ObjectMapper objectMapper;
    private final Path logFilePath;
    
    public RideLogger() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Create log directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(LOG_DIRECTORY));
        } catch (IOException e) {
            log.error("Failed to create log directory", e);
        }
        
        this.logFilePath = Paths.get(LOG_DIRECTORY, LOG_FILE);
    }
    
    /**
     * Log a ride to JSON file.
     * Appends ride data in a thread-safe manner.
     */
    public synchronized void logRide(Ride ride) {
        try {
            List<Ride> rides = readAllRides();
            rides.add(ride);
            
            String json = objectMapper.writeValueAsString(rides);
            Files.writeString(logFilePath, json, StandardOpenOption.CREATE, 
                            StandardOpenOption.TRUNCATE_EXISTING);
            
            log.info("Logged ride {} to file", ride.getId());
        } catch (IOException e) {
            log.error("Failed to log ride to file", e);
        }
    }
    
    /**
     * Read all rides from the log file.
     */
    public List<Ride> readAllRides() {
        try {
            if (!Files.exists(logFilePath)) {
                return new ArrayList<>();
            }
            
            String content = Files.readString(logFilePath);
            if (content.isBlank()) {
                return new ArrayList<>();
            }
            
            return objectMapper.readValue(content, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, Ride.class));
        } catch (IOException e) {
            log.error("Failed to read rides from file", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Clear all logged rides (for testing).
     */
    public synchronized void clearLogs() {
        try {
            Files.deleteIfExists(logFilePath);
            log.info("Cleared ride logs");
        } catch (IOException e) {
            log.error("Failed to clear logs", e);
        }
    }
}
