package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.dto.DoctorAffiliationRequest;
import com.healthconnect.finalbackendcapstone.dto.DoctorAffiliationResponse;
import com.healthconnect.finalbackendcapstone.service.DoctorAffiliationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-affiliation")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class DoctorAffiliationController {

    private final DoctorAffiliationService doctorAffiliationService;

    /**
     * Create a new affiliation record
     */
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorAffiliationResponse> createAffiliation(@Valid @RequestBody DoctorAffiliationRequest request) {
        DoctorAffiliationResponse response = doctorAffiliationService.createAffiliation(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get an affiliation record by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DoctorAffiliationResponse> getAffiliationById(@PathVariable Long id) {
        DoctorAffiliationResponse response = doctorAffiliationService.getAffiliationById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all affiliation records for a doctor
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorAffiliationResponse>> getAffiliationsByDoctorId(@PathVariable Long doctorId) {
        List<DoctorAffiliationResponse> response = doctorAffiliationService.getAffiliationsByDoctorId(doctorId);
        return ResponseEntity.ok(response);
    }

    /**
     * Search affiliations by institution name
     */
    @GetMapping("/search")
    public ResponseEntity<List<DoctorAffiliationResponse>> searchAffiliationsByInstitutionName(
            @RequestParam String institutionName) {
        List<DoctorAffiliationResponse> response = doctorAffiliationService.searchAffiliationsByInstitutionName(institutionName);
        return ResponseEntity.ok(response);
    }

    /**
     * Search doctor's affiliations by institution name
     */
    @GetMapping("/doctor/{doctorId}/search")
    public ResponseEntity<List<DoctorAffiliationResponse>> searchDoctorAffiliationsByInstitutionName(
            @PathVariable Long doctorId,
            @RequestParam String institutionName) {
        List<DoctorAffiliationResponse> response = 
                doctorAffiliationService.searchDoctorAffiliationsByInstitutionName(doctorId, institutionName);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an affiliation record
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorAffiliationResponse> updateAffiliation(
            @PathVariable Long id,
            @Valid @RequestBody DoctorAffiliationRequest request) {
        DoctorAffiliationResponse response = doctorAffiliationService.updateAffiliation(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an affiliation record
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteAffiliation(@PathVariable Long id) {
        doctorAffiliationService.deleteAffiliation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all affiliation records for a doctor
     */
    @DeleteMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteAllAffiliationsForDoctor(@PathVariable Long doctorId) {
        doctorAffiliationService.deleteAllAffiliationsForDoctor(doctorId);
        return ResponseEntity.noContent().build();
    }
} 