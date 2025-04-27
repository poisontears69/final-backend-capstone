package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.SubstanceUseRequest;
import com.healthconnect.finalbackendcapstone.dto.SubstanceUseResponse;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.PatientRecord;
import com.healthconnect.finalbackendcapstone.model.SubstanceUse;
import com.healthconnect.finalbackendcapstone.repository.PatientRecordRepository;
import com.healthconnect.finalbackendcapstone.repository.SubstanceUseRepository;
import com.healthconnect.finalbackendcapstone.util.UsageFrequencyFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubstanceUseService {
    private final SubstanceUseRepository substanceUseRepository;
    private final PatientRecordRepository patientRecordRepository;

    @Transactional
    public SubstanceUseResponse createSubstanceUse(SubstanceUseRequest request) {
        // Validate patient record exists
        PatientRecord patientRecord = patientRecordRepository.findById(request.getPatientRecordId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient record not found with id: " + request.getPatientRecordId()));
        
        // Validate end date is after start date if both are provided
        if (request.getStartDate() != null && request.getEndDate() != null 
                && request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        
        // Create new substance use record
        SubstanceUse substanceUse = new SubstanceUse();
        substanceUse.setPatientRecord(patientRecord);
        substanceUse.setSubstanceName(request.getSubstanceName());
        substanceUse.setUsageFrequency(request.getUsageFrequency());
        substanceUse.setStartDate(request.getStartDate());
        substanceUse.setEndDate(request.getEndDate());
        
        SubstanceUse savedSubstanceUse = substanceUseRepository.save(substanceUse);
        return mapToResponse(savedSubstanceUse);
    }
    
    @Transactional(readOnly = true)
    public SubstanceUseResponse getSubstanceUseById(Long id) {
        SubstanceUse substanceUse = substanceUseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Substance use record not found with id: " + id));
        return mapToResponse(substanceUse);
    }
    
    @Transactional(readOnly = true)
    public List<SubstanceUseResponse> getSubstanceUseByPatientRecord(Long patientRecordId) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        List<SubstanceUse> substanceUses = substanceUseRepository.findByPatientRecordId(patientRecordId);
        return substanceUses.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<SubstanceUseResponse> getSubstanceUseByPatientRecord(Long patientRecordId, Pageable pageable) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        Page<SubstanceUse> substanceUses = substanceUseRepository.findByPatientRecordId(patientRecordId, pageable);
        return substanceUses.map(this::mapToResponse);
    }
    
    @Transactional(readOnly = true)
    public List<SubstanceUseResponse> searchSubstanceUseByName(Long patientRecordId, String substanceName) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        List<SubstanceUse> substanceUses = substanceUseRepository
                .findByPatientRecordIdAndSubstanceNameContainingIgnoreCase(patientRecordId, substanceName);
        return substanceUses.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SubstanceUseResponse> getSubstanceUseByFrequency(
            Long patientRecordId, SubstanceUse.UsageFrequency frequency) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        List<SubstanceUse> substanceUses = substanceUseRepository
                .findByPatientRecordIdAndUsageFrequency(patientRecordId, frequency);
        return substanceUses.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SubstanceUseResponse> getCurrentSubstanceUse(Long patientRecordId) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        LocalDate currentDate = LocalDate.now();
        List<SubstanceUse> substanceUses = substanceUseRepository
                .findByPatientRecordIdAndEndDateIsNullOrEndDateGreaterThanEqual(patientRecordId, currentDate);
        return substanceUses.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SubstanceUseResponse> getPastSubstanceUse(Long patientRecordId) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        LocalDate currentDate = LocalDate.now();
        List<SubstanceUse> substanceUses = substanceUseRepository
                .findByPatientRecordIdAndEndDateLessThan(patientRecordId, currentDate);
        return substanceUses.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional
    public SubstanceUseResponse updateSubstanceUse(Long id, SubstanceUseRequest request) {
        SubstanceUse substanceUse = substanceUseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Substance use record not found with id: " + id));
        
        // Check if patient record is changing
        if (!substanceUse.getPatientRecord().getId().equals(request.getPatientRecordId())) {
            PatientRecord patientRecord = patientRecordRepository.findById(request.getPatientRecordId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Patient record not found with id: " + request.getPatientRecordId()));
            substanceUse.setPatientRecord(patientRecord);
        }
        
        // Validate end date is after start date if both are provided
        if (request.getStartDate() != null && request.getEndDate() != null 
                && request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        
        substanceUse.setSubstanceName(request.getSubstanceName());
        substanceUse.setUsageFrequency(request.getUsageFrequency());
        substanceUse.setStartDate(request.getStartDate());
        substanceUse.setEndDate(request.getEndDate());
        
        SubstanceUse updatedSubstanceUse = substanceUseRepository.save(substanceUse);
        return mapToResponse(updatedSubstanceUse);
    }
    
    @Transactional
    public void deleteSubstanceUse(Long id) {
        if (!substanceUseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Substance use record not found with id: " + id);
        }
        substanceUseRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteAllSubstanceUseForPatientRecord(Long patientRecordId) {
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        substanceUseRepository.deleteByPatientRecordId(patientRecordId);
    }
    
    private SubstanceUseResponse mapToResponse(SubstanceUse substanceUse) {
        // Determine if this is a current use
        boolean isCurrentUse = substanceUse.getEndDate() == null || 
                !substanceUse.getEndDate().isBefore(LocalDate.now());
        
        return SubstanceUseResponse.builder()
                .id(substanceUse.getId())
                .patientRecordId(substanceUse.getPatientRecord().getId())
                .patientName(substanceUse.getPatientRecord().getPatient().getFullName())
                .substanceName(substanceUse.getSubstanceName())
                .usageFrequency(substanceUse.getUsageFrequency())
                .usageFrequencyDisplay(UsageFrequencyFormatter.formatUsageFrequency(substanceUse.getUsageFrequency()))
                .startDate(substanceUse.getStartDate())
                .endDate(substanceUse.getEndDate())
                .createdAt(substanceUse.getCreatedAt())
                .isCurrentUse(isCurrentUse)
                .doctorName(substanceUse.getPatientRecord().getDoctor().getUser().getFullName())
                .build();
    }
} 