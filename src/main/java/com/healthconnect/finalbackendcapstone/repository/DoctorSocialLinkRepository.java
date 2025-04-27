package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.DoctorSocialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorSocialLinkRepository extends JpaRepository<DoctorSocialLink, Long> {
    List<DoctorSocialLink> findByDoctorId(Long doctorId);
    void deleteByDoctorId(Long doctorId);
} 