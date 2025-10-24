package com.ridesync.persistence.repository;

import com.ridesync.persistence.entity.RiderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for Rider entities
 */
@Repository
public interface RiderRepository extends JpaRepository<RiderEntity, String> {
    
    /**
     * Find rider by email
     */
    Optional<RiderEntity> findByEmail(String email);
    
    /**
     * Find rider by phone number
     */
    Optional<RiderEntity> findByPhoneNumber(String phoneNumber);
}
