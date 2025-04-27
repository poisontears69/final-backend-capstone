package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.Clinic;
import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {

    /**
     * Find clinics by doctor
     * @param doctor the doctor
     * @return list of clinics
     */
    List<Clinic> findByDoctor(DoctorProfile doctor);
    
    /**
     * Find clinics by doctor ID
     * @param doctorId the doctor ID
     * @return list of clinics
     */
    List<Clinic> findByDoctorId(Long doctorId);
    
    /**
     * Find clinics by doctor ID with pagination
     * @param doctorId the doctor ID
     * @param pageable pagination information
     * @return a page of clinics
     */
    Page<Clinic> findByDoctorId(Long doctorId, Pageable pageable);
    
    /**
     * Find clinics by city
     * @param city the city
     * @param pageable pagination information
     * @return a page of clinics
     */
    Page<Clinic> findByCity(String city, Pageable pageable);
    
    /**
     * Find clinics by clinic name containing the given string (case insensitive)
     * @param clinicName the clinic name to search for
     * @param pageable pagination information
     * @return a page of clinics
     */
    Page<Clinic> findByClinicNameContainingIgnoreCase(String clinicName, Pageable pageable);
    
    /**
     * Find a clinic by doctor ID and clinic ID
     * @param doctorId the doctor ID
     * @param clinicId the clinic ID
     * @return the clinic if found
     */
    Optional<Clinic> findByDoctorIdAndId(Long doctorId, Long clinicId);
    
    /**
     * Count clinics by doctor ID
     * @param doctorId the doctor ID
     * @return count of clinics
     */
    long countByDoctorId(Long doctorId);
} 