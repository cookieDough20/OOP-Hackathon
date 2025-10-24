package com.ridesync.persistence.repository;

import com.ridesync.core.model.enums.DriverStatus;
import com.ridesync.persistence.entity.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Driver entities
 */
@Repository
public interface DriverRepository extends JpaRepository<DriverEntity, String> {
    
    /**
     * Find all drivers by status
     */
    List<DriverEntity> findByStatus(DriverStatus status);
    
    /**
     * Find available drivers (for allocation)
     */
    default List<DriverEntity> findAvailableDrivers() {
        return findByStatus(DriverStatus.AVAILABLE);
    }
    
    /**
     * Find top earning drivers
     */
    List<DriverEntity> findTop10ByOrderByEarningsDesc();
}
