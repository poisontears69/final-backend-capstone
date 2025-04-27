package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {
    List<HealthRecord> findByPatientRecordId(Long patientRecordId);
    List<HealthRecord> findByType(String type);
    List<HealthRecord> findByResultDateBetween(LocalDate startDate, LocalDate endDate);
    List<HealthRecord> findByPatientRecordIdAndType(Long patientRecordId, String type);
} 