package com.ridesync.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Spring Boot application class for RideSync Solutions.
 * Hackathon-winning ride-sharing platform.
 */
@SpringBootApplication(scanBasePackages = "com.ridesync")
@EntityScan("com.ridesync.persistence.entity")
@EnableJpaRepositories("com.ridesync.persistence.repository")
public class RideSyncApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(RideSyncApplication.class, args);
    }
}
