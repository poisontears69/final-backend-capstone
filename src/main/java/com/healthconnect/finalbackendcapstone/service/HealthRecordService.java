package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.HealthRecordDTO;

import java.time.LocalDate;
import java.util.List;

public interface HealthRecordService {
    HealthRecordDTO createHealthRecord(HealthRecordDTO healthRecordDTO);
    HealthRecordDTO getHealthRecordById(Long id);
    List<HealthRecordDTO> getHealthRecordsByPatientRecordId(Long patientRecordId);
    List<HealthRecordDTO> getHealthRecordsByType(String type);
    List<HealthRecordDTO> getHealthRecordsByDateRange(LocalDate startDate, LocalDate endDate);
    List<HealthRecordDTO> getHealthRecordsByPatientRecordIdAndType(Long patientRecordId, String type);
    HealthRecordDTO updateHealthRecord(Long id, HealthRecordDTO healthRecordDTO);
    void deleteHealthRecord(Long id);
} 