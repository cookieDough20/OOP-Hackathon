package com.ridesync.persistence.repository;

import com.ridesync.core.model.RideStatus;
import com.ridesync.core.model.RideType;
import com.ridesync.persistence.entity.RideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for Ride entities.
 */
@Repository
public interface RideRepository extends JpaRepository<RideEntity, String> {
    
    /**
     * Find rides by rider ID.
     */
    List<RideEntity> findByRiderId(String riderId);
    
    /**
     * Find rides by driver ID.
     */
    List<RideEntity> findByDriverId(String driverId);
    
    /**
     * Find rides by status.
     */
    List<RideEntity> findByStatus(RideStatus status);
    
    /**
     * Find rides by type.
     */
    List<RideEntity> findByRideType(RideType rideType);
    
    /**
     * Find rides by status and type.
     */
    List<RideEntity> findByStatusAndRideType(RideStatus status, RideType rideType);
    
    /**
     * Find rides within date range.
     */
    List<RideEntity> findByRequestedAtBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * Calculate total earnings for a driver.
     */
    @Query("SELECT SUM(r.fare) FROM RideEntity r WHERE r.driverId = ?1 AND r.status = 'COMPLETED'")
    Double calculateTotalEarnings(String driverId);
    
    /**
     * Get ride count by status.
     */
    long countByStatus(RideStatus status);
}
