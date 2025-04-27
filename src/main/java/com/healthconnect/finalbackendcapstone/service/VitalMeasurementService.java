package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.VitalMeasurementRequest;
import com.healthconnect.finalbackendcapstone.dto.VitalMeasurementResponse;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.PatientRecord;
import com.healthconnect.finalbackendcapstone.model.VitalMeasurement;
import com.healthconnect.finalbackendcapstone.model.VitalType;
import com.healthconnect.finalbackendcapstone.repository.PatientRecordRepository;
import com.healthconnect.finalbackendcapstone.repository.VitalMeasurementRepository;
import com.healthconnect.finalbackendcapstone.repository.VitalTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VitalMeasurementService {
    private final VitalMeasurementRepository vitalMeasurementRepository;
    private final PatientRecordRepository patientRecordRepository;
    private final VitalTypeRepository vitalTypeRepository;

    @Transactional
    public VitalMeasurementResponse createVitalMeasurement(VitalMeasurementRequest request) {
        // Validate patient record exists
        PatientRecord patientRecord = patientRecordRepository.findById(request.getPatientRecordId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient record not found with id: " + request.getPatientRecordId()));
        
        // Validate vital type exists
        VitalType vitalType = vitalTypeRepository.findById(request.getVitalTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vital type not found with id: " + request.getVitalTypeId()));
        
        // Create new vital measurement
        VitalMeasurement vitalMeasurement = new VitalMeasurement();
        vitalMeasurement.setPatientRecord(patientRecord);
        vitalMeasurement.setVitalType(vitalType);
        vitalMeasurement.setValue(request.getValue());
        
        // Set recorded time if provided, otherwise default to now
        if (request.getRecordedAt() != null) {
            vitalMeasurement.setRecordedAt(request.getRecordedAt());
        }
        
        VitalMeasurement savedMeasurement = vitalMeasurementRepository.save(vitalMeasurement);
        return mapToResponse(savedMeasurement);
    }
    
    @Transactional(readOnly = true)
    public VitalMeasurementResponse getVitalMeasurementById(Long id) {
        VitalMeasurement vitalMeasurement = vitalMeasurementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vital measurement not found with id: " + id));
        return mapToResponse(vitalMeasurement);
    }
    
    @Transactional(readOnly = true)
    public List<VitalMeasurementResponse> getVitalMeasurementsByPatientRecord(Long patientRecordId) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        List<VitalMeasurement> measurements = vitalMeasurementRepository.findByPatientRecordId(patientRecordId);
        return measurements.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<VitalMeasurementResponse> getVitalMeasurementsByPatientRecord(
            Long patientRecordId, Pageable pageable) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        Page<VitalMeasurement> measurements = vitalMeasurementRepository.findByPatientRecordId(patientRecordId, pageable);
        return measurements.map(this::mapToResponse);
    }
    
    @Transactional(readOnly = true)
    public List<VitalMeasurementResponse> getVitalMeasurementsByPatientRecordAndType(
            Long patientRecordId, Integer vitalTypeId) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        // Verify vital type exists
        if (!vitalTypeRepository.existsById(vitalTypeId)) {
            throw new ResourceNotFoundException("Vital type not found with id: " + vitalTypeId);
        }
        
        List<VitalMeasurement> measurements = 
                vitalMeasurementRepository.findByPatientRecordIdAndVitalTypeId(patientRecordId, vitalTypeId);
        return measurements.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<VitalMeasurementResponse> getVitalMeasurementsByDateRange(
            Long patientRecordId, LocalDateTime startDate, LocalDateTime endDate) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        List<VitalMeasurement> measurements = vitalMeasurementRepository
                .findByPatientRecordIdAndRecordedAtBetween(patientRecordId, startDate, endDate);
        return measurements.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public VitalMeasurementResponse getLatestVitalMeasurement(Long patientRecordId, Integer vitalTypeId) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        // Verify vital type exists
        if (!vitalTypeRepository.existsById(vitalTypeId)) {
            throw new ResourceNotFoundException("Vital type not found with id: " + vitalTypeId);
        }
        
        // Get the most recent measurement for this vital type
        Page<VitalMeasurement> latestPage = vitalMeasurementRepository.findByPatientRecordIdAndVitalTypeIdOrderByRecordedAtDesc(
                patientRecordId, vitalTypeId, PageRequest.of(0, 1));
        
        if (latestPage.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No vital measurements found for patient record id: " + patientRecordId + 
                    " and vital type id: " + vitalTypeId);
        }
        
        return mapToResponse(latestPage.getContent().get(0));
    }
    
    @Transactional
    public VitalMeasurementResponse updateVitalMeasurement(Long id, VitalMeasurementRequest request) {
        VitalMeasurement vitalMeasurement = vitalMeasurementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vital measurement not found with id: " + id));
        
        // Check if patient record is changing
        if (!vitalMeasurement.getPatientRecord().getId().equals(request.getPatientRecordId())) {
            PatientRecord patientRecord = patientRecordRepository.findById(request.getPatientRecordId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Patient record not found with id: " + request.getPatientRecordId()));
            vitalMeasurement.setPatientRecord(patientRecord);
        }
        
        // Check if vital type is changing
        if (!vitalMeasurement.getVitalType().getId().equals(request.getVitalTypeId())) {
            VitalType vitalType = vitalTypeRepository.findById(request.getVitalTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Vital type not found with id: " + request.getVitalTypeId()));
            vitalMeasurement.setVitalType(vitalType);
        }
        
        vitalMeasurement.setValue(request.getValue());
        
        // Update recorded time if provided
        if (request.getRecordedAt() != null) {
            vitalMeasurement.setRecordedAt(request.getRecordedAt());
        }
        
        VitalMeasurement updatedMeasurement = vitalMeasurementRepository.save(vitalMeasurement);
        return mapToResponse(updatedMeasurement);
    }
    
    @Transactional
    public void deleteVitalMeasurement(Long id) {
        if (!vitalMeasurementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vital measurement not found with id: " + id);
        }
        vitalMeasurementRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteAllVitalMeasurementsForPatientRecord(Long patientRecordId) {
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        vitalMeasurementRepository.deleteByPatientRecordId(patientRecordId);
    }
    
    private VitalMeasurementResponse mapToResponse(VitalMeasurement vitalMeasurement) {
        return VitalMeasurementResponse.builder()
                .id(vitalMeasurement.getId())
                .patientRecordId(vitalMeasurement.getPatientRecord().getId())
                .vitalTypeId(vitalMeasurement.getVitalType().getId())
                .vitalTypeName(vitalMeasurement.getVitalType().getName())
                .value(vitalMeasurement.getValue())
                .unit(vitalMeasurement.getVitalType().getUnit())
                .recordedAt(vitalMeasurement.getRecordedAt())
                .createdAt(vitalMeasurement.getCreatedAt())
                .patientName(vitalMeasurement.getPatientRecord().getPatient().getFullName())
                .doctorName(vitalMeasurement.getPatientRecord().getDoctor().getUser().getFullName())
                .build();
    }
} 