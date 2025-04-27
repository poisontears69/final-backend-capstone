package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.MedicalProcedureRequest;
import com.healthconnect.finalbackendcapstone.dto.MedicalProcedureResponse;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.MedicalProcedure;
import com.healthconnect.finalbackendcapstone.model.PatientRecord;
import com.healthconnect.finalbackendcapstone.repository.MedicalProcedureRepository;
import com.healthconnect.finalbackendcapstone.repository.PatientRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalProcedureService {
    private final MedicalProcedureRepository medicalProcedureRepository;
    private final PatientRecordRepository patientRecordRepository;

    @Transactional
    public MedicalProcedureResponse createMedicalProcedure(MedicalProcedureRequest request) {
        // Validate patient record exists
        PatientRecord patientRecord = patientRecordRepository.findById(request.getPatientRecordId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient record not found with id: " + request.getPatientRecordId()));
        
        // Create new medical procedure
        MedicalProcedure procedure = new MedicalProcedure();
        procedure.setPatientRecord(patientRecord);
        procedure.setProcedureName(request.getProcedureName());
        procedure.setProcedureDate(request.getProcedureDate());
        procedure.setOutcome(request.getOutcome());
        
        MedicalProcedure savedProcedure = medicalProcedureRepository.save(procedure);
        return mapToResponse(savedProcedure);
    }
    
    @Transactional(readOnly = true)
    public MedicalProcedureResponse getMedicalProcedureById(Long id) {
        MedicalProcedure procedure = medicalProcedureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical procedure not found with id: " + id));
        return mapToResponse(procedure);
    }
    
    @Transactional(readOnly = true)
    public List<MedicalProcedureResponse> getMedicalProceduresByPatientRecord(Long patientRecordId) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        List<MedicalProcedure> procedures = medicalProcedureRepository.findByPatientRecordId(patientRecordId);
        return procedures.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<MedicalProcedureResponse> getMedicalProceduresByPatientRecord(Long patientRecordId, Pageable pageable) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        Page<MedicalProcedure> procedures = medicalProcedureRepository.findByPatientRecordId(patientRecordId, pageable);
        return procedures.map(this::mapToResponse);
    }
    
    @Transactional(readOnly = true)
    public List<MedicalProcedureResponse> searchMedicalProceduresByName(Long patientRecordId, String procedureName) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        List<MedicalProcedure> procedures = medicalProcedureRepository
                .findByPatientRecordIdAndProcedureNameContainingIgnoreCase(patientRecordId, procedureName);
        return procedures.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<MedicalProcedureResponse> getMedicalProceduresByDateRange(
            Long patientRecordId, LocalDate startDate, LocalDate endDate) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        // Validate date range
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        
        List<MedicalProcedure> procedures = medicalProcedureRepository
                .findByPatientRecordIdAndProcedureDateBetween(patientRecordId, startDate, endDate);
        return procedures.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<MedicalProcedureResponse> getMedicalProceduresByDate(Long patientRecordId, LocalDate procedureDate) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        List<MedicalProcedure> procedures = medicalProcedureRepository
                .findByPatientRecordIdAndProcedureDate(patientRecordId, procedureDate);
        return procedures.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<MedicalProcedureResponse> getRecentMedicalProcedures(Long patientRecordId, int limit) {
        // Verify patient record exists
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        
        Pageable pageable = PageRequest.of(0, limit, Sort.by("procedureDate").descending());
        Page<MedicalProcedure> procedures = medicalProcedureRepository
                .findByPatientRecordIdOrderByProcedureDateDesc(patientRecordId, pageable);
        return procedures.getContent().stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional
    public MedicalProcedureResponse updateMedicalProcedure(Long id, MedicalProcedureRequest request) {
        MedicalProcedure procedure = medicalProcedureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical procedure not found with id: " + id));
        
        // Check if patient record is changing
        if (!procedure.getPatientRecord().getId().equals(request.getPatientRecordId())) {
            PatientRecord patientRecord = patientRecordRepository.findById(request.getPatientRecordId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Patient record not found with id: " + request.getPatientRecordId()));
            procedure.setPatientRecord(patientRecord);
        }
        
        procedure.setProcedureName(request.getProcedureName());
        procedure.setProcedureDate(request.getProcedureDate());
        procedure.setOutcome(request.getOutcome());
        
        MedicalProcedure updatedProcedure = medicalProcedureRepository.save(procedure);
        return mapToResponse(updatedProcedure);
    }
    
    @Transactional
    public void deleteMedicalProcedure(Long id) {
        if (!medicalProcedureRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medical procedure not found with id: " + id);
        }
        medicalProcedureRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteAllMedicalProceduresForPatientRecord(Long patientRecordId) {
        if (!patientRecordRepository.existsById(patientRecordId)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + patientRecordId);
        }
        medicalProcedureRepository.deleteByPatientRecordId(patientRecordId);
    }
    
    private MedicalProcedureResponse mapToResponse(MedicalProcedure procedure) {
        return MedicalProcedureResponse.builder()
                .id(procedure.getId())
                .patientRecordId(procedure.getPatientRecord().getId())
                .patientName(procedure.getPatientRecord().getPatient().getFullName())
                .procedureName(procedure.getProcedureName())
                .procedureDate(procedure.getProcedureDate())
                .outcome(procedure.getOutcome())
                .createdAt(procedure.getCreatedAt())
                .doctorName(procedure.getPatientRecord().getDoctor().getUser().getFullName())
                .patientAge(procedure.getPatientRecord().getPatient().getAge())
                .patientGender(procedure.getPatientRecord().getPatient().getGender())
                .build();
    }
} 