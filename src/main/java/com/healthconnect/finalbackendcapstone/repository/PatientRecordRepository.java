package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import com.healthconnect.finalbackendcapstone.model.Patient;
import com.healthconnect.finalbackendcapstone.model.PatientRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRecordRepository extends JpaRepository<PatientRecord, Long> {

    /**
     * Find patient records by patient
     * @param patient the patient
     * @param pageable pagination information
     * @return a page of patient records
     */
    Page<PatientRecord> findByPatient(Patient patient, Pageable pageable);
    
    /**
     * Find patient records by patient ID
     * @param patientId the patient ID
     * @param pageable pagination information
     * @return a page of patient records
     */
    Page<PatientRecord> findByPatientId(Long patientId, Pageable pageable);
    
    /**
     * Find patient records by doctor
     * @param doctor the doctor
     * @param pageable pagination information
     * @return a page of patient records
     */
    Page<PatientRecord> findByDoctor(DoctorProfile doctor, Pageable pageable);
    
    /**
     * Find patient records by doctor ID
     * @param doctorId the doctor ID
     * @param pageable pagination information
     * @return a page of patient records
     */
    Page<PatientRecord> findByDoctorId(Long doctorId, Pageable pageable);
    
    /**
     * Find a specific patient record by patient ID and doctor ID
     * @param patientId the patient ID
     * @param doctorId the doctor ID
     * @return the patient record if found
     */
    Optional<PatientRecord> findByPatientIdAndDoctorId(Long patientId, Long doctorId);
    
    /**
     * Check if a patient record exists by patient ID and doctor ID
     * @param patientId the patient ID
     * @param doctorId the doctor ID
     * @return true if exists, false otherwise
     */
    boolean existsByPatientIdAndDoctorId(Long patientId, Long doctorId);
    
    /**
     * Get count of patient records by doctor ID
     * @param doctorId the doctor ID
     * @return the count
     */
    long countByDoctorId(Long doctorId);
    
    /**
     * Get count of patient records by patient ID
     * @param patientId the patient ID
     * @return the count
     */
    long countByPatientId(Long patientId);
} 