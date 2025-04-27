package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.DoctorEducation;
import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorEducationRepository extends JpaRepository<DoctorEducation, Long> {

    /**
     * Find all education records for a specific doctor
     * @param doctor the doctor
     * @return list of education records
     */
    List<DoctorEducation> findByDoctor(DoctorProfile doctor);
    
    /**
     * Find all education records for a specific doctor ID
     * @param doctorId the doctor ID
     * @return list of education records
     */
    List<DoctorEducation> findByDoctorId(Long doctorId);
    
    /**
     * Find all education records for a specific doctor ID and education type
     * @param doctorId the doctor ID
     * @param educationType the education type
     * @return list of education records
     */
    List<DoctorEducation> findByDoctorIdAndEducationType(Long doctorId, DoctorEducation.EducationType educationType);
    
    /**
     * Count education records for a specific doctor ID
     * @param doctorId the doctor ID
     * @return count of education records
     */
    long countByDoctorId(Long doctorId);
    
    /**
     * Count education records for a specific doctor ID and education type
     * @param doctorId the doctor ID
     * @param educationType the education type
     * @return count of education records
     */
    long countByDoctorIdAndEducationType(Long doctorId, DoctorEducation.EducationType educationType);
    
    /**
     * Delete all education records for a specific doctor ID
     * @param doctorId the doctor ID
     */
    void deleteByDoctorId(Long doctorId);
} 