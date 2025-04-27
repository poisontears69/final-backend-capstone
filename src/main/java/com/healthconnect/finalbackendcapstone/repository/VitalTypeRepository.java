package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.VitalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VitalTypeRepository extends JpaRepository<VitalType, Integer> {
    
    /**
     * Find a vital type by its name (case insensitive)
     * @param name the vital type name
     * @return the vital type if found
     */
    Optional<VitalType> findByNameIgnoreCase(String name);
    
    /**
     * Find vital types by name containing the given string (case insensitive)
     * @param name the name to search for
     * @return list of vital types
     */
    List<VitalType> findByNameContainingIgnoreCase(String name);
    
    /**
     * Check if a vital type exists by its name (case insensitive)
     * @param name the vital type name
     * @return true if exists, false otherwise
     */
    boolean existsByNameIgnoreCase(String name);
} 