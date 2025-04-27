package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.DoctorSocialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorSocialLinkRepository extends JpaRepository<DoctorSocialLink, Long> {

    /**
     * Find all social links for a specific doctor ID
     * @param doctorId the doctor ID
     * @return list of social links
     */
    List<DoctorSocialLink> findByDoctorId(Long doctorId);
    
    /**
     * Delete all social links for a specific doctor ID
     * @param doctorId the doctor ID
     */
    void deleteByDoctorId(Long doctorId);
} 