package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.SubstanceUse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubstanceUseRepository extends JpaRepository<SubstanceUse, Long> {
    
    /**
     * Find all substance use records for a patient record
     * @param patientRecordId the patient record ID
     * @return list of substance use records
     */
    List<SubstanceUse> findByPatientRecordId(Long patientRecordId);
    
    /**
     * Find all substance use records for a patient record with pagination
     * @param patientRecordId the patient record ID
     * @param pageable pagination information
     * @return page of substance use records
     */
    Page<SubstanceUse> findByPatientRecordId(Long patientRecordId, Pageable pageable);
    
    /**
     * Find all substance use records for a patient record by substance name
     * @param patientRecordId the patient record ID
     * @param substanceName the substance name
     * @return list of substance use records
     */
    List<SubstanceUse> findByPatientRecordIdAndSubstanceNameContainingIgnoreCase(Long patientRecordId, String substanceName);
    
    /**
     * Find all substance use records for a patient record by usage frequency
     * @param patientRecordId the patient record ID
     * @param usageFrequency the usage frequency
     * @return list of substance use records
     */
    List<SubstanceUse> findByPatientRecordIdAndUsageFrequency(
            Long patientRecordId, SubstanceUse.UsageFrequency usageFrequency);
    
    /**
     * Find all substance use records for a patient record with current use (no end date or end date in the future)
     * @param patientRecordId the patient record ID
     * @param currentDate the current date to compare against
     * @return list of substance use records
     */
    List<SubstanceUse> findByPatientRecordIdAndEndDateIsNullOrEndDateGreaterThanEqual(
            Long patientRecordId, LocalDate currentDate);
    
    /**
     * Find all substance use records for a patient record with former use (end date in the past)
     * @param patientRecordId the patient record ID
     * @param currentDate the current date to compare against
     * @return list of substance use records
     */
    List<SubstanceUse> findByPatientRecordIdAndEndDateLessThan(Long patientRecordId, LocalDate currentDate);
    
    /**
     * Count all substance use records for a patient record
     * @param patientRecordId the patient record ID
     * @return count of substance use records
     */
    long countByPatientRecordId(Long patientRecordId);
    
    /**
     * Delete all substance use records for a patient record
     * @param patientRecordId the patient record ID
     */
    void deleteByPatientRecordId(Long patientRecordId);
} 