package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.dto.DoctorSocialLinkRequest;
import com.healthconnect.finalbackendcapstone.dto.DoctorSocialLinkResponse;
import com.healthconnect.finalbackendcapstone.service.DoctorSocialLinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-social-link")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class DoctorSocialLinkController {

    private final DoctorSocialLinkService doctorSocialLinkService;

    /**
     * Create a new social link record
     */
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorSocialLinkResponse> createSocialLink(@Valid @RequestBody DoctorSocialLinkRequest request) {
        DoctorSocialLinkResponse response = doctorSocialLinkService.createSocialLink(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get a social link record by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DoctorSocialLinkResponse> getSocialLinkById(@PathVariable Long id) {
        DoctorSocialLinkResponse response = doctorSocialLinkService.getSocialLinkById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all social link records for a doctor
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorSocialLinkResponse>> getSocialLinksByDoctorId(@PathVariable Long doctorId) {
        List<DoctorSocialLinkResponse> response = doctorSocialLinkService.getSocialLinksByDoctorId(doctorId);
        return ResponseEntity.ok(response);
    }

    /**
     * Update a social link record
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorSocialLinkResponse> updateSocialLink(
            @PathVariable Long id,
            @Valid @RequestBody DoctorSocialLinkRequest request) {
        DoctorSocialLinkResponse response = doctorSocialLinkService.updateSocialLink(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a social link record
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteSocialLink(@PathVariable Long id) {
        doctorSocialLinkService.deleteSocialLink(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all social link records for a doctor
     */
    @DeleteMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteAllSocialLinksForDoctor(@PathVariable Long doctorId) {
        doctorSocialLinkService.deleteAllSocialLinksForDoctor(doctorId);
        return ResponseEntity.noContent().build();
    }
} 