package com.ridesync.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Spring Boot Application for RideSync Solutions
 * Production-ready ride-sharing platform
 * 
 * @author RideSync Team
 * @version 1.0.0
 */
@SpringBootApplication(scanBasePackages = "com.ridesync")
@EnableJpaRepositories(basePackages = "com.ridesync.persistence.repository")
@EntityScan(basePackages = "com.ridesync.persistence.entity")
public class RideSyncApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(RideSyncApplication.class, args);
        
        System.out.println("""
            
            ╔══════════════════════════════════════════════════════╗
            ║                                                      ║
            ║        RideSync Solutions Started Successfully!      ║
            ║                                                      ║
            ║   Swagger UI: http://localhost:8080/swagger-ui.html ║
            ║   Actuator:   http://localhost:8080/actuator/health ║
            ║   H2 Console: http://localhost:8080/h2-console      ║
            ║                                                      ║
            ╚══════════════════════════════════════════════════════╝
            """);
    }
}
