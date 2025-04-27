package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.dto.ClinicRequest;
import com.healthconnect.finalbackendcapstone.dto.ClinicResponse;
import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.service.ClinicService;
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

import java.util.List;

@RestController
@RequestMapping("/api/clinics")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ClinicController {

    private final ClinicService clinicService;

    /**
     * Create a new clinic
     */
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ClinicResponse> createClinic(@Valid @RequestBody ClinicRequest request) {
        ClinicResponse response = clinicService.createClinic(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get a clinic by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClinicResponse> getClinicById(@PathVariable Long id) {
        ClinicResponse response = clinicService.getClinicById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get clinics by doctor ID
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<ClinicResponse>> getClinicsByDoctorId(@PathVariable Long doctorId) {
        List<ClinicResponse> response = clinicService.getClinicsByDoctorId(doctorId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get clinics by doctor ID with pagination
     */
    @GetMapping("/doctor/{doctorId}/page")
    public ResponseEntity<Page<ClinicResponse>> getClinicsByDoctorIdPaginated(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "clinicName") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<ClinicResponse> response = clinicService.getClinicsByDoctorId(doctorId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get clinics by city with pagination
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<Page<ClinicResponse>> getClinicsByCity(
            @PathVariable String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "clinicName") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<ClinicResponse> response = clinicService.getClinicsByCity(city, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Search clinics by name with pagination
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ClinicResponse>> searchClinicsByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "clinicName") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<ClinicResponse> response = clinicService.searchClinicsByName(name, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Update a clinic
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ClinicResponse> updateClinic(
            @PathVariable Long id,
            @Valid @RequestBody ClinicRequest request) {
        ClinicResponse response = clinicService.updateClinic(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a clinic
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteClinic(@PathVariable Long id) {
        clinicService.deleteClinic(id);
        return ResponseEntity.noContent().build();
    }
} 