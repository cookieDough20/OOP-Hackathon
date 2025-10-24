package com.ridesync.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for API documentation
 */
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI rideSyncOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("RideSync Solutions API")
                .description("""
                    Production-ready ride-sharing platform with Spring Boot 3.x
                    
                    ## Features
                    - Real-time ride booking and tracking
                    - Dynamic surge pricing with ML-like heuristics
                    - Multiple ride types (Standard, Pool, Luxury)
                    - Thread-safe concurrent booking support
                    - WebSocket for real-time updates
                    - Analytics and reporting APIs
                    - H2 in-memory database with JPA
                    - File-based ride logging with JSON serialization
                    
                    ## Design Patterns
                    - Factory Pattern for ride creation
                    - Strategy Pattern for fare calculation
                    - Singleton Pattern for ride allocation
                    
                    ## Demo Flow
                    1. Register riders and drivers (POST /api/riders, /api/drivers)
                    2. Book a ride (POST /api/bookRide)
                    3. Track ride status (GET /api/rides/{rideId})
                    4. Complete ride (POST /api/completeRide/{rideId})
                    5. View analytics (GET /api/analytics/*)
                    """)
                .version("1.0.0")
                .contact(new Contact()
                    .name("RideSync Team")
                    .email("team@ridesync.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")
                    .description("Local Development Server")
            ));
    }
}
