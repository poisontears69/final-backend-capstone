package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.DoctorAffiliation;
import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorAffiliationRepository extends JpaRepository<DoctorAffiliation, Long> {

    /**
     * Find all affiliations for a specific doctor
     * @param doctor the doctor
     * @return list of affiliations
     */
    List<DoctorAffiliation> findByDoctor(DoctorProfile doctor);
    
    /**
     * Find all affiliations for a specific doctor ID
     * @param doctorId the doctor ID
     * @return list of affiliations
     */
    List<DoctorAffiliation> findByDoctorId(Long doctorId);
    
    /**
     * Find all affiliations by institution name containing the given string (case insensitive)
     * @param institutionName the institution name to search for
     * @return list of affiliations
     */
    List<DoctorAffiliation> findByInstitutionNameContainingIgnoreCase(String institutionName);
    
    /**
     * Find all affiliations for a specific doctor ID and institution name containing the given string (case insensitive)
     * @param doctorId the doctor ID
     * @param institutionName the institution name to search for
     * @return list of affiliations
     */
    List<DoctorAffiliation> findByDoctorIdAndInstitutionNameContainingIgnoreCase(Long doctorId, String institutionName);
    
    /**
     * Count affiliations for a specific doctor ID
     * @param doctorId the doctor ID
     * @return count of affiliations
     */
    long countByDoctorId(Long doctorId);
    
    /**
     * Delete all affiliations for a specific doctor ID
     * @param doctorId the doctor ID
     */
    void deleteByDoctorId(Long doctorId);
} 