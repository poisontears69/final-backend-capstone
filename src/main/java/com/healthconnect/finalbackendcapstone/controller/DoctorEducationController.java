package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.dto.DoctorEducationRequest;
import com.healthconnect.finalbackendcapstone.dto.DoctorEducationResponse;
import com.healthconnect.finalbackendcapstone.model.DoctorEducation;
import com.healthconnect.finalbackendcapstone.service.DoctorEducationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-education")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class DoctorEducationController {

    private final DoctorEducationService doctorEducationService;

    /**
     * Create a new education record
     */
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorEducationResponse> createEducation(@Valid @RequestBody DoctorEducationRequest request) {
        DoctorEducationResponse response = doctorEducationService.createEducation(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get an education record by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DoctorEducationResponse> getEducationById(@PathVariable Long id) {
        DoctorEducationResponse response = doctorEducationService.getEducationById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all education records for a doctor
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorEducationResponse>> getEducationByDoctorId(@PathVariable Long doctorId) {
        List<DoctorEducationResponse> response = doctorEducationService.getEducationByDoctorId(doctorId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get education records by doctor ID and type
     */
    @GetMapping("/doctor/{doctorId}/type/{type}")
    public ResponseEntity<List<DoctorEducationResponse>> getEducationByDoctorIdAndType(
            @PathVariable Long doctorId,
            @PathVariable DoctorEducation.EducationType type) {
        List<DoctorEducationResponse> response = doctorEducationService.getEducationByDoctorIdAndType(doctorId, type);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an education record
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorEducationResponse> updateEducation(
            @PathVariable Long id,
            @Valid @RequestBody DoctorEducationRequest request) {
        DoctorEducationResponse response = doctorEducationService.updateEducation(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an education record
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id) {
        doctorEducationService.deleteEducation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all education records for a doctor
     */
    @DeleteMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteAllEducationForDoctor(@PathVariable Long doctorId) {
        doctorEducationService.deleteAllEducationForDoctor(doctorId);
        return ResponseEntity.noContent().build();
    }
} 