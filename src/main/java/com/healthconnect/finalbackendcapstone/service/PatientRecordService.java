package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.PatientRecordRequest;
import com.healthconnect.finalbackendcapstone.dto.PatientRecordResponse;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import com.healthconnect.finalbackendcapstone.model.Patient;
import com.healthconnect.finalbackendcapstone.model.PatientRecord;
import com.healthconnect.finalbackendcapstone.repository.DoctorProfileRepository;
import com.healthconnect.finalbackendcapstone.repository.PatientRecordRepository;
import com.healthconnect.finalbackendcapstone.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientRecordService {
    private final PatientRecordRepository patientRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    @Transactional
    public PatientRecordResponse createPatientRecord(PatientRecordRequest request) {
        // Get patient and doctor entities
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        DoctorProfile doctor = doctorProfileRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));

        // Check if record already exists
        boolean exists = patientRecordRepository.existsByPatientIdAndDoctorId(
                request.getPatientId(), request.getDoctorId());
        if (exists) {
            throw new IllegalStateException("Patient record already exists for this patient and doctor");
        }

        // Create new record
        PatientRecord record = new PatientRecord();
        record.setPatient(patient);
        record.setDoctor(doctor);

        PatientRecord savedRecord = patientRecordRepository.save(record);
        return mapToResponse(savedRecord);
    }

    @Transactional(readOnly = true)
    public PatientRecordResponse getPatientRecord(Long patientId, Long doctorId) {
        PatientRecord record = patientRecordRepository.findByPatientIdAndDoctorId(patientId, doctorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient record not found for patient id: " + patientId + " and doctor id: " + doctorId));
        return mapToResponse(record);
    }

    @Transactional(readOnly = true)
    public PatientRecordResponse getPatientRecordById(Long id) {
        PatientRecord record = patientRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient record not found with id: " + id));
        return mapToResponse(record);
    }

    @Transactional(readOnly = true)
    public Page<PatientRecordResponse> getPatientRecordsByPatient(Long patientId, Pageable pageable) {
        Page<PatientRecord> records = patientRecordRepository.findByPatientId(patientId, pageable);
        return records.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<PatientRecordResponse> getPatientRecordsByDoctor(Long doctorId, Pageable pageable) {
        Page<PatientRecord> records = patientRecordRepository.findByDoctorId(doctorId, pageable);
        return records.map(this::mapToResponse);
    }

    @Transactional
    public void deletePatientRecord(Long id) {
        if (!patientRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient record not found with id: " + id);
        }
        patientRecordRepository.deleteById(id);
    }

    @Transactional
    public void deletePatientRecordByPatientAndDoctor(Long patientId, Long doctorId) {
        Optional<PatientRecord> record = patientRecordRepository.findByPatientIdAndDoctorId(patientId, doctorId);
        if (record.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Patient record not found for patient id: " + patientId + " and doctor id: " + doctorId);
        }
        patientRecordRepository.delete(record.get());
    }
    
    private PatientRecordResponse mapToResponse(PatientRecord record) {
        return PatientRecordResponse.builder()
                .id(record.getId())
                .patientId(record.getPatient().getId())
                .patientName(record.getPatient().getFullName())
                .doctorId(record.getDoctor().getId())
                .doctorName(record.getDoctor().getUser().getFullName())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .patientGender(record.getPatient().getGender())
                .patientAge(record.getPatient().getAge())
                .patientEmail(record.getPatient().getUser().getEmail())
                .doctorSpecialty(record.getDoctor().getTitle())
                .doctorEmail(record.getDoctor().getEmail())
                .doctorPhoneNumber(record.getDoctor().getPhoneNumber())
                .build();
    }
} 