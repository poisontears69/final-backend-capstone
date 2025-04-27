package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.DoctorCertification;
import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorCertificationRepository extends JpaRepository<DoctorCertification, Long> {

    /**
     * Find all certifications for a specific doctor
     * @param doctor the doctor
     * @return list of certifications
     */
    List<DoctorCertification> findByDoctor(DoctorProfile doctor);
    
    /**
     * Find all certifications for a specific doctor ID
     * @param doctorId the doctor ID
     * @return list of certifications
     */
    List<DoctorCertification> findByDoctorId(Long doctorId);
    
    /**
     * Find all certifications for a specific doctor ID and certification type
     * @param doctorId the doctor ID
     * @param certificationType the certification type
     * @return list of certifications
     */
    List<DoctorCertification> findByDoctorIdAndCertificationType(
            Long doctorId, 
            DoctorCertification.CertificationType certificationType);
    
    /**
     * Count certifications for a specific doctor ID
     * @param doctorId the doctor ID
     * @return count of certifications
     */
    long countByDoctorId(Long doctorId);
    
    /**
     * Count certifications for a specific doctor ID and certification type
     * @param doctorId the doctor ID
     * @param certificationType the certification type
     * @return count of certifications
     */
    long countByDoctorIdAndCertificationType(
            Long doctorId, 
            DoctorCertification.CertificationType certificationType);
    
    /**
     * Delete all certifications for a specific doctor ID
     * @param doctorId the doctor ID
     */
    void deleteByDoctorId(Long doctorId);
} 