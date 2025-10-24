package com.ridesync.persistence.repository;

import com.ridesync.persistence.entity.RiderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Rider entities.
 */
@Repository
public interface RiderRepository extends JpaRepository<RiderEntity, String> {
    
    /**
     * Find riders by phone number.
     */
    RiderEntity findByPhone(String phone);
    
    /**
     * Find riders with high ratings.
     */
    List<RiderEntity> findByRatingGreaterThanEqual(double rating);
}
