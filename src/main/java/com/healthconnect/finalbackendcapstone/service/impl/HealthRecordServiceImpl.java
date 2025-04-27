package com.healthconnect.finalbackendcapstone.service.impl;

import com.healthconnect.finalbackendcapstone.dto.HealthRecordDTO;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.HealthRecord;
import com.healthconnect.finalbackendcapstone.model.PatientRecord;
import com.healthconnect.finalbackendcapstone.repository.HealthRecordRepository;
import com.healthconnect.finalbackendcapstone.repository.PatientRecordRepository;
import com.healthconnect.finalbackendcapstone.service.HealthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HealthRecordServiceImpl implements HealthRecordService {

    private final HealthRecordRepository healthRecordRepository;
    private final PatientRecordRepository patientRecordRepository;

    @Autowired
    public HealthRecordServiceImpl(HealthRecordRepository healthRecordRepository,
                                  PatientRecordRepository patientRecordRepository) {
        this.healthRecordRepository = healthRecordRepository;
        this.patientRecordRepository = patientRecordRepository;
    }

    @Override
    public HealthRecordDTO createHealthRecord(HealthRecordDTO healthRecordDTO) {
        HealthRecord healthRecord = mapToEntity(healthRecordDTO);
        HealthRecord savedHealthRecord = healthRecordRepository.save(healthRecord);
        return mapToDTO(savedHealthRecord);
    }

    @Override
    public HealthRecordDTO getHealthRecordById(Long id) {
        HealthRecord healthRecord = healthRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HealthRecord", "id", id));
        return mapToDTO(healthRecord);
    }

    @Override
    public List<HealthRecordDTO> getHealthRecordsByPatientRecordId(Long patientRecordId) {
        List<HealthRecord> healthRecords = healthRecordRepository.findByPatientRecordId(patientRecordId);
        return healthRecords.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthRecordDTO> getHealthRecordsByType(String type) {
        List<HealthRecord> healthRecords = healthRecordRepository.findByType(type);
        return healthRecords.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthRecordDTO> getHealthRecordsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<HealthRecord> healthRecords = healthRecordRepository.findByResultDateBetween(startDate, endDate);
        return healthRecords.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthRecordDTO> getHealthRecordsByPatientRecordIdAndType(Long patientRecordId, String type) {
        List<HealthRecord> healthRecords = healthRecordRepository.findByPatientRecordIdAndType(patientRecordId, type);
        return healthRecords.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HealthRecordDTO updateHealthRecord(Long id, HealthRecordDTO healthRecordDTO) {
        HealthRecord healthRecord = healthRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HealthRecord", "id", id));
        
        healthRecord.setType(healthRecordDTO.getType());
        healthRecord.setResultDate(healthRecordDTO.getResultDate());
        healthRecord.setLabName(healthRecordDTO.getLabName());
        healthRecord.setRemarks(healthRecordDTO.getRemarks());
        
        HealthRecord updatedHealthRecord = healthRecordRepository.save(healthRecord);
        return mapToDTO(updatedHealthRecord);
    }

    @Override
    public void deleteHealthRecord(Long id) {
        HealthRecord healthRecord = healthRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HealthRecord", "id", id));
        healthRecordRepository.delete(healthRecord);
    }

    private HealthRecord mapToEntity(HealthRecordDTO healthRecordDTO) {
        HealthRecord healthRecord = new HealthRecord();
        
        if (healthRecordDTO.getId() != null) {
            healthRecord.setId(healthRecordDTO.getId());
        }
        
        healthRecord.setType(healthRecordDTO.getType());
        healthRecord.setResultDate(healthRecordDTO.getResultDate());
        healthRecord.setLabName(healthRecordDTO.getLabName());
        healthRecord.setRemarks(healthRecordDTO.getRemarks());
        
        if (healthRecordDTO.getPatientRecordId() != null) {
            PatientRecord patientRecord = patientRecordRepository.findById(healthRecordDTO.getPatientRecordId())
                    .orElseThrow(() -> new ResourceNotFoundException("PatientRecord", "id", healthRecordDTO.getPatientRecordId()));
            healthRecord.setPatientRecord(patientRecord);
        }
        
        return healthRecord;
    }

    private HealthRecordDTO mapToDTO(HealthRecord healthRecord) {
        HealthRecordDTO healthRecordDTO = new HealthRecordDTO();
        
        healthRecordDTO.setId(healthRecord.getId());
        healthRecordDTO.setType(healthRecord.getType());
        healthRecordDTO.setResultDate(healthRecord.getResultDate());
        healthRecordDTO.setLabName(healthRecord.getLabName());
        healthRecordDTO.setRemarks(healthRecord.getRemarks());
        healthRecordDTO.setCreatedAt(healthRecord.getCreatedAt());
        
        if (healthRecord.getPatientRecord() != null) {
            healthRecordDTO.setPatientRecordId(healthRecord.getPatientRecord().getId());
        }
        
        return healthRecordDTO;
    }
} 