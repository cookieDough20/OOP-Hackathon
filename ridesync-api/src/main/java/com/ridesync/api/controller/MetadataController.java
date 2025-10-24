package com.ridesync.api.controller;

import com.ridesync.core.model.Ride;
import com.ridesync.core.model.Driver;
import com.ridesync.core.model.Rider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Controller demonstrating Reflection API usage.
 * Provides metadata about domain classes for debugging and documentation.
 */
@RestController
@RequestMapping("/api/metadata")
@Tag(name = "Metadata & Reflection", description = "Debug APIs using Java Reflection")
public class MetadataController {
    
    @GetMapping("/class/{className}")
    @Operation(summary = "Get class metadata", 
               description = "Use Reflection to inspect class structure")
    public ResponseEntity<ClassMetadata> getClassMetadata(@PathVariable String className) {
        try {
            Class<?> clazz = getClassByName(className);
            
            if (clazz == null) {
                return ResponseEntity.notFound().build();
            }
            
            List<String> fields = new ArrayList<>();
            for (Field field : clazz.getDeclaredFields()) {
                fields.add(field.getType().getSimpleName() + " " + field.getName());
            }
            
            List<String> methods = new ArrayList<>();
            for (Method method : clazz.getDeclaredMethods()) {
                methods.add(method.getReturnType().getSimpleName() + " " + 
                           method.getName() + "()");
            }
            
            ClassMetadata metadata = new ClassMetadata(
                clazz.getSimpleName(),
                clazz.getName(),
                clazz.getSuperclass() != null ? clazz.getSuperclass().getSimpleName() : "None",
                Arrays.stream(clazz.getInterfaces())
                    .map(Class::getSimpleName)
                    .toList(),
                fields,
                methods
            );
            
            return ResponseEntity.ok(metadata);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/flowchart")
    @Operation(summary = "Get system flowchart", 
               description = "ASCII art representation of ride booking flow")
    public ResponseEntity<Map<String, Object>> getFlowchart() {
        String asciiFlowchart = """
                ┌─────────────────────────────────────────────────────────────┐
                │                  RIDESYNC RIDE BOOKING FLOW                 │
                └─────────────────────────────────────────────────────────────┘
                
                     ┌──────────┐
                     │  Rider   │
                     │ Requests │
                     │   Ride   │
                     └────┬─────┘
                          │
                          ▼
                   ┌──────────────┐
                   │ RideFactory  │──── Creates ride based on type
                   │ (Factory)    │     (Standard/Pool/Luxury)
                   └──────┬───────┘
                          │
                          ▼
                ┌────────────────────┐
                │  SurgePricing      │──── Calculates dynamic surge
                │  Service           │     based on time & demand
                └────────┬───────────┘
                         │
                         ▼
                ┌────────────────────┐
                │  RideAllocator     │──── Thread-safe allocation
                │  (Singleton)       │     Find nearest driver
                └────────┬───────────┘
                         │
                         ▼
                ┌────────────────────┐
                │  FareStrategy      │──── Calculate fare using
                │  (Strategy)        │     appropriate strategy
                └────────┬───────────┘
                         │
                         ▼
                ┌────────────────────┐
                │  Persist to DB     │──── Save to H2 database
                │  & Log to File     │     & JSON file
                └────────┬───────────┘
                         │
                         ▼
                ┌────────────────────┐
                │  WebSocket Notify  │──── Real-time update
                │  Rider             │     to rider
                └────────┬───────────┘
                         │
                         ▼
                   ┌───────────┐
                   │  Driver   │
                   │  Arrives  │
                   └───────────┘
                """;
        
        List<String> steps = List.of(
            "1. Rider submits ride request (POST /api/rides/book)",
            "2. RideFactory creates appropriate ride type",
            "3. SurgePricingService calculates surge multiplier",
            "4. RideAllocator finds nearest available driver",
            "5. FareStrategy calculates fare with surge",
            "6. Ride saved to database and JSON log file",
            "7. WebSocket notification sent to rider",
            "8. Driver accepts and starts ride",
            "9. Ride completed, earnings calculated",
            "10. Analytics updated via Stream operations"
        );
        
        Map<String, Object> flowchart = new HashMap<>();
        flowchart.put("asciiArt", asciiFlowchart);
        flowchart.put("steps", steps);
        flowchart.put("designPatterns", List.of("Factory", "Strategy", "Singleton", "Template Method"));
        
        return ResponseEntity.ok(flowchart);
    }
    
    private Class<?> getClassByName(String className) {
        try {
            return switch (className.toLowerCase()) {
                case "ride" -> Ride.class;
                case "driver" -> Driver.class;
                case "rider" -> Rider.class;
                default -> Class.forName("com.ridesync.core.model." + className);
            };
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    @Data
    @AllArgsConstructor
    static class ClassMetadata {
        private String className;
        private String fullName;
        private String superClass;
        private List<String> interfaces;
        private List<String> fields;
        private List<String> methods;
    }
}
