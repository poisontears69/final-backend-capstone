package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.HealthRecordPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthRecordPhotoRepository extends JpaRepository<HealthRecordPhoto, Long> {
    List<HealthRecordPhoto> findByHealthRecordId(Long healthRecordId);
    void deleteByHealthRecordId(Long healthRecordId);
} 