package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.Medication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    
    /**
     * Find all medications for a patient record
     * @param patientRecordId the patient record ID
     * @return list of medications
     */
    List<Medication> findByPatientRecordId(Long patientRecordId);
    
    /**
     * Find all medications for a patient record with pagination
     * @param patientRecordId the patient record ID
     * @param pageable pagination information
     * @return page of medications
     */
    Page<Medication> findByPatientRecordId(Long patientRecordId, Pageable pageable);
    
    /**
     * Find medications by patient record ID and medication name
     * @param patientRecordId the patient record ID
     * @param medicationName the medication name
     * @return list of medications
     */
    List<Medication> findByPatientRecordIdAndMedicationNameContainingIgnoreCase(Long patientRecordId, String medicationName);
    
    /**
     * Find current medications for a patient record (where end_date is null or >= today)
     * @param patientRecordId the patient record ID
     * @param currentDate the current date to compare against
     * @return list of current medications
     */
    @Query("SELECT m FROM Medication m WHERE m.patientRecord.id = ?1 AND (m.endDate IS NULL OR m.endDate >= ?2)")
    List<Medication> findCurrentMedications(Long patientRecordId, LocalDate currentDate);
    
    /**
     * Find medications by patient record ID and date range
     * @param patientRecordId the patient record ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of medications
     */
    @Query("SELECT m FROM Medication m WHERE m.patientRecord.id = ?1 AND " +
           "((m.startDate BETWEEN ?2 AND ?3) OR (m.endDate BETWEEN ?2 AND ?3) OR " +
           "(m.startDate <= ?2 AND (m.endDate IS NULL OR m.endDate >= ?3)))")
    List<Medication> findByPatientRecordIdAndDateRange(Long patientRecordId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Find medications by patient record ID that are active on a specific date
     * @param patientRecordId the patient record ID
     * @param activeDate the date to check for active medications
     * @return list of medications
     */
    @Query("SELECT m FROM Medication m WHERE m.patientRecord.id = ?1 AND " +
           "m.startDate <= ?2 AND (m.endDate IS NULL OR m.endDate >= ?2)")
    List<Medication> findActiveOnDate(Long patientRecordId, LocalDate activeDate);
    
    /**
     * Count medications by patient record ID
     * @param patientRecordId the patient record ID
     * @return count of medications
     */
    long countByPatientRecordId(Long patientRecordId);
    
    /**
     * Delete all medications for a patient record
     * @param patientRecordId the patient record ID
     */
    void deleteByPatientRecordId(Long patientRecordId);

    List<Medication> findByPatientRecord_PatientId(Long patientId);
    
    List<Medication> findByPatientRecord_DoctorId(Long doctorId);
} 