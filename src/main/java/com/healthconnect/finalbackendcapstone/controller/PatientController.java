package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.dto.PatientRequest;
import com.healthconnect.finalbackendcapstone.dto.PatientResponse;
import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class PatientController {

    private final PatientService patientService;

    /**
     * Create a patient profile for the currently authenticated user
     */
    @PostMapping("/profile")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponse> createPatientProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody PatientRequest request) {
        PatientResponse response = patientService.createPatient(user.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get the profile of the currently authenticated patient
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponse> getCurrentPatientProfile(@AuthenticationPrincipal User user) {
        PatientResponse response = patientService.getPatientByUserId(user.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * Update the profile of the currently authenticated patient
     */
    @PutMapping("/profile")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponse> updateCurrentPatientProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody PatientRequest request) {
        PatientResponse currentProfile = patientService.getPatientByUserId(user.getId());
        PatientResponse response = patientService.updatePatient(currentProfile.getId(), request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a patient profile by ID (for doctors and admins)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<PatientResponse> getPatientProfileById(@PathVariable Long id) {
        PatientResponse response = patientService.getPatientById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Search for patients (for doctors and admins)
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Page<PatientResponse>> searchPatients(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<PatientResponse> response = patientService.searchPatients(query, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get patients by city (for doctors and admins)
     */
    @GetMapping("/city/{city}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Page<PatientResponse>> getPatientsByCity(
            @PathVariable String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PatientResponse> response = patientService.getPatientsByCity(city, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get patients by province (for doctors and admins)
     */
    @GetMapping("/province/{province}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Page<PatientResponse>> getPatientsByProvince(
            @PathVariable String province,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PatientResponse> response = patientService.getPatientsByProvince(province, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a patient profile (admin or owner patient only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<Void> deletePatientProfile(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        // If not admin, ensure the patient is only deleting their own profile
        if (user.getRole() != User.Role.ADMIN) {
            PatientResponse profile = patientService.getPatientById(id);
            if (!profile.getUserId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
} 