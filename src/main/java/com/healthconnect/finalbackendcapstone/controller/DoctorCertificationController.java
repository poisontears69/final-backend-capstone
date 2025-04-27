package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.dto.DoctorCertificationRequest;
import com.healthconnect.finalbackendcapstone.dto.DoctorCertificationResponse;
import com.healthconnect.finalbackendcapstone.model.DoctorCertification;
import com.healthconnect.finalbackendcapstone.service.DoctorCertificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-certification")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class DoctorCertificationController {

    private final DoctorCertificationService doctorCertificationService;

    /**
     * Create a new certification record
     */
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorCertificationResponse> createCertification(@Valid @RequestBody DoctorCertificationRequest request) {
        DoctorCertificationResponse response = doctorCertificationService.createCertification(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get a certification record by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DoctorCertificationResponse> getCertificationById(@PathVariable Long id) {
        DoctorCertificationResponse response = doctorCertificationService.getCertificationById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all certification records for a doctor
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorCertificationResponse>> getCertificationsByDoctorId(@PathVariable Long doctorId) {
        List<DoctorCertificationResponse> response = doctorCertificationService.getCertificationsByDoctorId(doctorId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get certification records by doctor ID and type
     */
    @GetMapping("/doctor/{doctorId}/type/{type}")
    public ResponseEntity<List<DoctorCertificationResponse>> getCertificationsByDoctorIdAndType(
            @PathVariable Long doctorId,
            @PathVariable DoctorCertification.CertificationType type) {
        List<DoctorCertificationResponse> response = doctorCertificationService.getCertificationsByDoctorIdAndType(doctorId, type);
        return ResponseEntity.ok(response);
    }

    /**
     * Update a certification record
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorCertificationResponse> updateCertification(
            @PathVariable Long id,
            @Valid @RequestBody DoctorCertificationRequest request) {
        DoctorCertificationResponse response = doctorCertificationService.updateCertification(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a certification record
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteCertification(@PathVariable Long id) {
        doctorCertificationService.deleteCertification(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all certification records for a doctor
     */
    @DeleteMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteAllCertificationsForDoctor(@PathVariable Long doctorId) {
        doctorCertificationService.deleteAllCertificationsForDoctor(doctorId);
        return ResponseEntity.noContent().build();
    }
} 