package com.ridesync.persistence.repository;

import com.ridesync.core.model.DriverStatus;
import com.ridesync.persistence.entity.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Driver entities.
 */
@Repository
public interface DriverRepository extends JpaRepository<DriverEntity, String> {
    
    /**
     * Find all drivers by status.
     */
    List<DriverEntity> findByStatus(DriverStatus status);
    
    /**
     * Find drivers with rating above threshold.
     */
    List<DriverEntity> findByRatingGreaterThanEqual(double rating);
    
    /**
     * Count available drivers.
     */
    long countByStatus(DriverStatus status);
}
