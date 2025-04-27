package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.PatientRequest;
import com.healthconnect.finalbackendcapstone.dto.PatientResponse;
import com.healthconnect.finalbackendcapstone.exception.DuplicateResourceException;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.Patient;
import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.repository.PatientRepository;
import com.healthconnect.finalbackendcapstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    /**
     * Create a new patient profile
     * @param userId the user ID
     * @param request the patient request
     * @return the created patient
     */
    @Transactional
    public PatientResponse createPatient(Long userId, PatientRequest request) {
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Check if user has role PATIENT
        if (user.getRole() != User.Role.PATIENT) {
            throw new IllegalArgumentException("User must have PATIENT role to create a patient profile");
        }
        
        // Check if user already has a patient profile
        if (patientRepository.existsByUserId(userId)) {
            throw new DuplicateResourceException("Patient profile already exists for this user");
        }
        
        // Create patient
        Patient patient = new Patient();
        patient.setUser(user);
        updatePatientFromRequest(patient, request);
        
        // Save patient
        Patient savedPatient = patientRepository.save(patient);
        
        // Return response
        return buildPatientResponse(savedPatient);
    }
    
    /**
     * Update an existing patient
     * @param patientId the patient ID
     * @param request the patient request
     * @return the updated patient
     */
    @Transactional
    public PatientResponse updatePatient(Long patientId, PatientRequest request) {
        // Find existing patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        
        // Update patient
        updatePatientFromRequest(patient, request);
        
        // Save patient
        Patient savedPatient = patientRepository.save(patient);
        
        // Return response
        return buildPatientResponse(savedPatient);
    }
    
    /**
     * Get a patient by ID
     * @param patientId the patient ID
     * @return the patient
     */
    public PatientResponse getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        
        return buildPatientResponse(patient);
    }
    
    /**
     * Get a patient by user ID
     * @param userId the user ID
     * @return the patient
     */
    public PatientResponse getPatientByUserId(Long userId) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found for user id: " + userId));
        
        return buildPatientResponse(patient);
    }
    
    /**
     * Search for patients by name
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return a page of patients
     */
    public Page<PatientResponse> searchPatients(String searchTerm, Pageable pageable) {
        Page<Patient> patients = patientRepository.searchPatients(searchTerm, pageable);
        
        return patients.map(this::buildPatientResponse);
    }
    
    /**
     * Find patients by city
     * @param city the city
     * @param pageable pagination information
     * @return a page of patients
     */
    public Page<PatientResponse> getPatientsByCity(String city, Pageable pageable) {
        Page<Patient> patients = patientRepository.findByCity(city, pageable);
        
        return patients.map(this::buildPatientResponse);
    }
    
    /**
     * Find patients by province
     * @param province the province
     * @param pageable pagination information
     * @return a page of patients
     */
    public Page<PatientResponse> getPatientsByProvince(String province, Pageable pageable) {
        Page<Patient> patients = patientRepository.findByProvince(province, pageable);
        
        return patients.map(this::buildPatientResponse);
    }
    
    /**
     * Find patients by birthday range
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return a page of patients
     */
    public Page<PatientResponse> getPatientsByBirthdayRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Patient> patients = patientRepository.findByBirthdayBetween(startDate, endDate, pageable);
        
        return patients.map(this::buildPatientResponse);
    }
    
    /**
     * Find patients by gender
     * @param gender the gender
     * @param pageable pagination information
     * @return a page of patients
     */
    public Page<PatientResponse> getPatientsByGender(String gender, Pageable pageable) {
        Page<Patient> patients = patientRepository.findByGender(gender, pageable);
        
        return patients.map(this::buildPatientResponse);
    }
    
    /**
     * Delete a patient
     * @param patientId the patient ID
     */
    @Transactional
    public void deletePatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        
        patientRepository.deleteById(patientId);
    }
    
    /**
     * Helper method to update patient from request
     * @param patient the patient to update
     * @param request the request with new data
     */
    private void updatePatientFromRequest(Patient patient, PatientRequest request) {
        patient.setFirstName(request.getFirstName());
        patient.setMiddleName(request.getMiddleName());
        patient.setLastName(request.getLastName());
        patient.setBirthday(request.getBirthday());
        patient.setGender(request.getGender());
        patient.setAddressLine1(request.getAddressLine1());
        patient.setAddressLine2(request.getAddressLine2());
        patient.setCity(request.getCity());
        patient.setProvince(request.getProvince());
        patient.setZipCode(request.getZipCode());
        patient.setProfilePictureUrl(request.getProfilePictureUrl());
    }
    
    /**
     * Helper method to build patient response from entity
     * @param patient the patient entity
     * @return the patient response
     */
    private PatientResponse buildPatientResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .userId(patient.getUser().getId())
                .firstName(patient.getFirstName())
                .middleName(patient.getMiddleName())
                .lastName(patient.getLastName())
                .fullName(patient.getFullName())
                .birthday(patient.getBirthday())
                .age(PatientResponse.calculateAge(patient.getBirthday()))
                .gender(patient.getGender())
                .addressLine1(patient.getAddressLine1())
                .addressLine2(patient.getAddressLine2())
                .city(patient.getCity())
                .province(patient.getProvince())
                .zipCode(patient.getZipCode())
                .fullAddress(patient.getFullAddress())
                .profilePictureUrl(patient.getProfilePictureUrl())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }
} 