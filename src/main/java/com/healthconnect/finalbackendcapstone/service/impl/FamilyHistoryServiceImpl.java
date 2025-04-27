package com.healthconnect.finalbackendcapstone.service.impl;

import com.healthconnect.finalbackendcapstone.dto.FamilyHistoryDTO;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.FamilyHistory;
import com.healthconnect.finalbackendcapstone.model.FamilyHistory.RelativeType;
import com.healthconnect.finalbackendcapstone.model.PatientRecord;
import com.healthconnect.finalbackendcapstone.repository.FamilyHistoryRepository;
import com.healthconnect.finalbackendcapstone.repository.PatientRecordRepository;
import com.healthconnect.finalbackendcapstone.service.FamilyHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FamilyHistoryServiceImpl implements FamilyHistoryService {

    private final FamilyHistoryRepository familyHistoryRepository;
    private final PatientRecordRepository patientRecordRepository;

    @Autowired
    public FamilyHistoryServiceImpl(FamilyHistoryRepository familyHistoryRepository,
                                   PatientRecordRepository patientRecordRepository) {
        this.familyHistoryRepository = familyHistoryRepository;
        this.patientRecordRepository = patientRecordRepository;
    }

    @Override
    public FamilyHistoryDTO createFamilyHistory(FamilyHistoryDTO familyHistoryDTO) {
        FamilyHistory familyHistory = mapToEntity(familyHistoryDTO);
        FamilyHistory savedFamilyHistory = familyHistoryRepository.save(familyHistory);
        return mapToDTO(savedFamilyHistory);
    }

    @Override
    public FamilyHistoryDTO getFamilyHistoryById(Long id) {
        FamilyHistory familyHistory = familyHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FamilyHistory", "id", id));
        return mapToDTO(familyHistory);
    }

    @Override
    public List<FamilyHistoryDTO> getFamilyHistoriesByPatientRecordId(Long patientRecordId) {
        List<FamilyHistory> familyHistories = familyHistoryRepository.findByPatientRecordId(patientRecordId);
        return familyHistories.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FamilyHistoryDTO> getFamilyHistoriesByHealthCondition(String healthCondition) {
        List<FamilyHistory> familyHistories = familyHistoryRepository.findByHealthConditionContainingIgnoreCase(healthCondition);
        return familyHistories.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FamilyHistoryDTO> getFamilyHistoriesByRelative(RelativeType relative) {
        List<FamilyHistory> familyHistories = familyHistoryRepository.findByRelative(relative);
        return familyHistories.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FamilyHistoryDTO> getFamilyHistoriesByPatientRecordIdAndRelative(Long patientRecordId, RelativeType relative) {
        List<FamilyHistory> familyHistories = familyHistoryRepository.findByPatientRecordIdAndRelative(patientRecordId, relative);
        return familyHistories.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FamilyHistoryDTO> getFamilyHistoriesByPatientRecordIdAndHealthCondition(Long patientRecordId, String healthCondition) {
        List<FamilyHistory> familyHistories = familyHistoryRepository.findByPatientRecordIdAndHealthConditionContainingIgnoreCase(patientRecordId, healthCondition);
        return familyHistories.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FamilyHistoryDTO updateFamilyHistory(Long id, FamilyHistoryDTO familyHistoryDTO) {
        FamilyHistory familyHistory = familyHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FamilyHistory", "id", id));
        
        familyHistory.setHealthCondition(familyHistoryDTO.getHealthCondition());
        familyHistory.setRelative(familyHistoryDTO.getRelative());
        familyHistory.setAgeAtDiagnosis(familyHistoryDTO.getAgeAtDiagnosis());
        
        FamilyHistory updatedFamilyHistory = familyHistoryRepository.save(familyHistory);
        return mapToDTO(updatedFamilyHistory);
    }

    @Override
    public void deleteFamilyHistory(Long id) {
        FamilyHistory familyHistory = familyHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FamilyHistory", "id", id));
        familyHistoryRepository.delete(familyHistory);
    }

    private FamilyHistory mapToEntity(FamilyHistoryDTO familyHistoryDTO) {
        FamilyHistory familyHistory = new FamilyHistory();
        
        if (familyHistoryDTO.getId() != null) {
            familyHistory.setId(familyHistoryDTO.getId());
        }
        
        familyHistory.setHealthCondition(familyHistoryDTO.getHealthCondition());
        familyHistory.setRelative(familyHistoryDTO.getRelative());
        familyHistory.setAgeAtDiagnosis(familyHistoryDTO.getAgeAtDiagnosis());
        
        if (familyHistoryDTO.getPatientRecordId() != null) {
            PatientRecord patientRecord = patientRecordRepository.findById(familyHistoryDTO.getPatientRecordId())
                    .orElseThrow(() -> new ResourceNotFoundException("PatientRecord", "id", familyHistoryDTO.getPatientRecordId()));
            familyHistory.setPatientRecord(patientRecord);
        }
        
        return familyHistory;
    }

    private FamilyHistoryDTO mapToDTO(FamilyHistory familyHistory) {
        FamilyHistoryDTO familyHistoryDTO = new FamilyHistoryDTO();
        
        familyHistoryDTO.setId(familyHistory.getId());
        familyHistoryDTO.setHealthCondition(familyHistory.getHealthCondition());
        familyHistoryDTO.setRelative(familyHistory.getRelative());
        familyHistoryDTO.setAgeAtDiagnosis(familyHistory.getAgeAtDiagnosis());
        familyHistoryDTO.setCreatedAt(familyHistory.getCreatedAt());
        
        if (familyHistory.getPatientRecord() != null) {
            familyHistoryDTO.setPatientRecordId(familyHistory.getPatientRecord().getId());
        }
        
        return familyHistoryDTO;
    }
} 