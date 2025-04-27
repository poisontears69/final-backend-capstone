package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.MedicalProcedure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicalProcedureRepository extends JpaRepository<MedicalProcedure, Long> {
    
    /**
     * Find all medical procedures for a patient record
     * @param patientRecordId the patient record ID
     * @return list of medical procedures
     */
    List<MedicalProcedure> findByPatientRecordId(Long patientRecordId);
    
    /**
     * Find all medical procedures for a patient record with pagination
     * @param patientRecordId the patient record ID
     * @param pageable pagination information
     * @return page of medical procedures
     */
    Page<MedicalProcedure> findByPatientRecordId(Long patientRecordId, Pageable pageable);
    
    /**
     * Find medical procedures by patient record ID and procedure name
     * @param patientRecordId the patient record ID
     * @param procedureName the procedure name
     * @return list of medical procedures
     */
    List<MedicalProcedure> findByPatientRecordIdAndProcedureNameContainingIgnoreCase(Long patientRecordId, String procedureName);
    
    /**
     * Find medical procedures by patient record ID and date range
     * @param patientRecordId the patient record ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of medical procedures
     */
    List<MedicalProcedure> findByPatientRecordIdAndProcedureDateBetween(
            Long patientRecordId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Find medical procedures by patient record ID and procedure date
     * @param patientRecordId the patient record ID
     * @param procedureDate the procedure date
     * @return list of medical procedures
     */
    List<MedicalProcedure> findByPatientRecordIdAndProcedureDate(Long patientRecordId, LocalDate procedureDate);
    
    /**
     * Find most recent medical procedures by patient record ID
     * @param patientRecordId the patient record ID
     * @param pageable pagination information (use PageRequest.of(0, limit, Sort.by("procedureDate").descending()))
     * @return page of medical procedures
     */
    Page<MedicalProcedure> findByPatientRecordIdOrderByProcedureDateDesc(Long patientRecordId, Pageable pageable);
    
    /**
     * Count medical procedures by patient record ID
     * @param patientRecordId the patient record ID
     * @return count of medical procedures
     */
    long countByPatientRecordId(Long patientRecordId);
    
    /**
     * Delete all medical procedures for a patient record
     * @param patientRecordId the patient record ID
     */
    void deleteByPatientRecordId(Long patientRecordId);
} 