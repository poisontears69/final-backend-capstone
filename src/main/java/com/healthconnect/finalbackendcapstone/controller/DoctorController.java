package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.dto.DoctorProfileRequest;
import com.healthconnect.finalbackendcapstone.dto.DoctorProfileResponse;
import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.service.DoctorProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * This controller acts as a compatibility layer for frontend applications
 * that use the /api/doctor/ endpoints (without 's')
 */
@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class DoctorController {

    private final DoctorProfileService doctorProfileService;

    /**
     * Create a doctor profile for the currently authenticated user
     */
    @PostMapping("/profile")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorProfileResponse> createDoctorProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody DoctorProfileRequest request) {
        DoctorProfileResponse response = doctorProfileService.createDoctorProfile(user.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * Get the profile of the currently authenticated doctor
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorProfileResponse> getCurrentDoctorProfile(@AuthenticationPrincipal User user) {
        DoctorProfileResponse response = doctorProfileService.getDoctorProfileByUserId(user.getId());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get a doctor profile by user ID
     */
    @GetMapping("/profile/user/{userId}")
    public ResponseEntity<DoctorProfileResponse> getDoctorProfileByUserId(@PathVariable Long userId) {
        DoctorProfileResponse response = doctorProfileService.getDoctorProfileByUserId(userId);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update the profile of the currently authenticated doctor
     */
    @PutMapping("/profile")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorProfileResponse> updateCurrentDoctorProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody DoctorProfileRequest request) {
        // First get the doctor profile to get the profile ID
        DoctorProfileResponse currentProfile = doctorProfileService.getDoctorProfileByUserId(user.getId());
        DoctorProfileResponse response = doctorProfileService.updateDoctorProfile(currentProfile.getId(), request);
        return ResponseEntity.ok(response);
    }
} 