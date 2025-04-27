package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.FamilyHistoryDTO;
import com.healthconnect.finalbackendcapstone.model.FamilyHistory.RelativeType;

import java.util.List;

public interface FamilyHistoryService {
    FamilyHistoryDTO createFamilyHistory(FamilyHistoryDTO familyHistoryDTO);
    FamilyHistoryDTO getFamilyHistoryById(Long id);
    List<FamilyHistoryDTO> getFamilyHistoriesByPatientRecordId(Long patientRecordId);
    List<FamilyHistoryDTO> getFamilyHistoriesByHealthCondition(String healthCondition);
    List<FamilyHistoryDTO> getFamilyHistoriesByRelative(RelativeType relative);
    List<FamilyHistoryDTO> getFamilyHistoriesByPatientRecordIdAndRelative(Long patientRecordId, RelativeType relative);
    List<FamilyHistoryDTO> getFamilyHistoriesByPatientRecordIdAndHealthCondition(Long patientRecordId, String healthCondition);
    FamilyHistoryDTO updateFamilyHistory(Long id, FamilyHistoryDTO familyHistoryDTO);
    void deleteFamilyHistory(Long id);
} 