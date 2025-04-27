package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.dto.DoctorProfileRequest;
import com.healthconnect.finalbackendcapstone.dto.DoctorProfileResponse;
import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.service.DoctorProfileService;
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
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class DoctorProfileController {

    private final DoctorProfileService doctorProfileService;

    /**
     * Create a doctor profile for the currently authenticated user
     * @param user the authenticated user
     * @param request the doctor profile request
     * @return the created doctor profile
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
     * @param user the authenticated user
     * @return the doctor profile
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorProfileResponse> getCurrentDoctorProfile(@AuthenticationPrincipal User user) {
        DoctorProfileResponse response = doctorProfileService.getDoctorProfileByUserId(user.getId());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update the profile of the currently authenticated doctor
     * @param user the authenticated user
     * @param request the doctor profile request
     * @return the updated doctor profile
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
    
    /**
     * Get a doctor profile by ID
     * @param id the doctor profile ID
     * @return the doctor profile
     */
    @GetMapping("/{id}")
    public ResponseEntity<DoctorProfileResponse> getDoctorProfileById(@PathVariable Long id) {
        DoctorProfileResponse response = doctorProfileService.getDoctorProfileById(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search for doctors
     * @param query the search query
     * @param page the page number (0-based)
     * @param size the page size
     * @param sort the sort field
     * @param direction the sort direction (asc or desc)
     * @return a page of doctor profiles
     */
    @GetMapping("/search")
    public ResponseEntity<Page<DoctorProfileResponse>> searchDoctors(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size, 
                Sort.Direction.fromString(direction), sort);
        Page<DoctorProfileResponse> response = doctorProfileService.searchDoctors(query, pageable);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all verified doctors
     * @param page the page number (0-based)
     * @param size the page size
     * @param sort the sort field
     * @param direction the sort direction (asc or desc)
     * @return a page of verified doctor profiles
     */
    @GetMapping("/verified")
    public ResponseEntity<Page<DoctorProfileResponse>> getVerifiedDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size, 
                Sort.Direction.fromString(direction), sort);
        Page<DoctorProfileResponse> response = doctorProfileService.getVerifiedDoctors(pageable);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get doctors by experience level
     * @param years minimum years of experience
     * @param page the page number (0-based)
     * @param size the page size
     * @param sort the sort field
     * @param direction the sort direction (asc or desc)
     * @return a page of doctor profiles
     */
    @GetMapping("/experience/{years}")
    public ResponseEntity<Page<DoctorProfileResponse>> getDoctorsByExperience(
            @PathVariable int years,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "practicingSince") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size, 
                Sort.Direction.fromString(direction), sort);
        Page<DoctorProfileResponse> response = doctorProfileService.getDoctorsByExperience(years, pageable);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Verify a doctor profile (admin only)
     * @param id the doctor profile ID
     * @return the verified doctor profile
     */
    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorProfileResponse> verifyDoctorProfile(@PathVariable Long id) {
        DoctorProfileResponse response = doctorProfileService.verifyDoctorProfile(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Delete a doctor profile (admin or owner doctor only)
     * @param id the doctor profile ID
     * @param user the authenticated user
     * @return no content response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteDoctorProfile(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        // If not admin, ensure the doctor is only deleting their own profile
        if (user.getRole() != User.Role.ADMIN) {
            DoctorProfileResponse profile = doctorProfileService.getDoctorProfileById(id);
            if (!profile.getUserId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        
        doctorProfileService.deleteDoctorProfile(id);
        return ResponseEntity.noContent().build();
    }
} 