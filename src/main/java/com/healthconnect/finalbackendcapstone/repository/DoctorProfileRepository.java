package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import com.healthconnect.finalbackendcapstone.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {

    /**
     * Find a doctor profile by user
     * @param user the user
     * @return the doctor profile if found
     */
    Optional<DoctorProfile> findByUser(User user);
    
    /**
     * Find a doctor profile by user ID
     * @param userId the user ID
     * @return the doctor profile if found
     */
    Optional<DoctorProfile> findByUserId(Long userId);
    
    /**
     * Find a doctor profile by PRC number
     * @param prcNumber the PRC number
     * @return the doctor profile if found
     */
    Optional<DoctorProfile> findByPrcNumber(String prcNumber);
    
    /**
     * Check if a doctor profile with the given PRC number exists
     * @param prcNumber the PRC number
     * @return true if exists, false otherwise
     */
    boolean existsByPrcNumber(String prcNumber);
    
    /**
     * Search for doctors by name or specialization
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return a page of doctor profiles
     */
    @Query("SELECT d FROM DoctorProfile d WHERE " +
           "LOWER(d.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<DoctorProfile> searchDoctors(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    /**
     * Find all verified doctors
     * @param pageable pagination information
     * @return a page of verified doctor profiles
     */
    Page<DoctorProfile> findByIsVerifiedTrue(Pageable pageable);
    
    /**
     * Find doctors by years of experience (practicing since)
     * @param minYears minimum years (current year - practicing since)
     * @param pageable pagination information
     * @return a page of doctor profiles
     */
    @Query("SELECT d FROM DoctorProfile d WHERE " +
           "d.practicingSince <= :yearThreshold")
    Page<DoctorProfile> findByExperienceGreaterThanEqual(@Param("yearThreshold") int yearThreshold, Pageable pageable);
} 