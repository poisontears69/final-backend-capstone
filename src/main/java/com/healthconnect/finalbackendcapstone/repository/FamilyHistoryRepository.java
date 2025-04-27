package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.FamilyHistory;
import com.healthconnect.finalbackendcapstone.model.FamilyHistory.RelativeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyHistoryRepository extends JpaRepository<FamilyHistory, Long> {
    List<FamilyHistory> findByPatientRecordId(Long patientRecordId);
    List<FamilyHistory> findByHealthConditionContainingIgnoreCase(String healthCondition);
    List<FamilyHistory> findByRelative(RelativeType relative);
    List<FamilyHistory> findByPatientRecordIdAndRelative(Long patientRecordId, RelativeType relative);
    List<FamilyHistory> findByPatientRecordIdAndHealthConditionContainingIgnoreCase(Long patientRecordId, String healthCondition);
} 