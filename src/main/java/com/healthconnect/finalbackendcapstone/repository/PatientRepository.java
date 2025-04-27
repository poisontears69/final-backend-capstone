package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.Patient;
import com.healthconnect.finalbackendcapstone.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Find a patient by user
     * @param user the user
     * @return the patient if found
     */
    Optional<Patient> findByUser(User user);
    
    /**
     * Find a patient by user ID
     * @param userId the user ID
     * @return the patient if found
     */
    Optional<Patient> findByUserId(Long userId);
    
    /**
     * Search for patients by name
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return a page of patients
     */
    @Query("SELECT p FROM Patient p WHERE " +
           "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Patient> searchPatients(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    /**
     * Find patients by city
     * @param city the city
     * @param pageable pagination information
     * @return a page of patients
     */
    Page<Patient> findByCity(String city, Pageable pageable);
    
    /**
     * Find patients by province
     * @param province the province
     * @param pageable pagination information
     * @return a page of patients
     */
    Page<Patient> findByProvince(String province, Pageable pageable);
    
    /**
     * Find patients by birthday range
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return a page of patients
     */
    Page<Patient> findByBirthdayBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find patients by gender
     * @param gender the gender
     * @param pageable pagination information
     * @return a page of patients
     */
    Page<Patient> findByGender(String gender, Pageable pageable);
    
    /**
     * Check if a patient exists for the given user ID
     * @param userId the user ID
     * @return true if exists, false otherwise
     */
    boolean existsByUserId(Long userId);
} 