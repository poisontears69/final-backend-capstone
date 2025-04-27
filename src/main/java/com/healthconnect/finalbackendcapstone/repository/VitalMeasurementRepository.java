package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.VitalMeasurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VitalMeasurementRepository extends JpaRepository<VitalMeasurement, Long> {
    
    /**
     * Find vital measurements by patient record ID
     * @param patientRecordId the patient record ID
     * @return list of vital measurements
     */
    List<VitalMeasurement> findByPatientRecordId(Long patientRecordId);
    
    /**
     * Find vital measurements by patient record ID with pagination
     * @param patientRecordId the patient record ID
     * @param pageable pagination information
     * @return page of vital measurements
     */
    Page<VitalMeasurement> findByPatientRecordId(Long patientRecordId, Pageable pageable);
    
    /**
     * Find vital measurements by patient record ID and vital type ID
     * @param patientRecordId the patient record ID
     * @param vitalTypeId the vital type ID
     * @return list of vital measurements
     */
    List<VitalMeasurement> findByPatientRecordIdAndVitalTypeId(Long patientRecordId, Integer vitalTypeId);
    
    /**
     * Find vital measurements by patient record ID and vital type ID with pagination
     * @param patientRecordId the patient record ID
     * @param vitalTypeId the vital type ID
     * @param pageable pagination information
     * @return page of vital measurements
     */
    Page<VitalMeasurement> findByPatientRecordIdAndVitalTypeId(
            Long patientRecordId, Integer vitalTypeId, Pageable pageable);
    
    /**
     * Find vital measurements by patient record ID and recorded date range
     * @param patientRecordId the patient record ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of vital measurements
     */
    List<VitalMeasurement> findByPatientRecordIdAndRecordedAtBetween(
            Long patientRecordId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find vital measurements by patient record ID, vital type ID, and recorded date range
     * @param patientRecordId the patient record ID
     * @param vitalTypeId the vital type ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of vital measurements
     */
    List<VitalMeasurement> findByPatientRecordIdAndVitalTypeIdAndRecordedAtBetween(
            Long patientRecordId, Integer vitalTypeId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find most recent vital measurement by patient record ID and vital type ID
     * @param patientRecordId the patient record ID
     * @param vitalTypeId the vital type ID
     * @param pageable pagination information (use PageRequest.of(0, 1, Sort.by("recordedAt").descending()))
     * @return page of vital measurements
     */
    Page<VitalMeasurement> findByPatientRecordIdAndVitalTypeIdOrderByRecordedAtDesc(
            Long patientRecordId, Integer vitalTypeId, Pageable pageable);
    
    /**
     * Delete all vital measurements for a patient record
     * @param patientRecordId the patient record ID
     */
    void deleteByPatientRecordId(Long patientRecordId);
} 