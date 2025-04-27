package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.dto.ClinicScheduleRequest;
import com.healthconnect.finalbackendcapstone.dto.ClinicScheduleResponse;
import com.healthconnect.finalbackendcapstone.service.ClinicScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api/clinic-schedules")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ClinicScheduleController {

    private final ClinicScheduleService clinicScheduleService;

    /**
     * Create a new clinic schedule
     */
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ClinicScheduleResponse> createSchedule(@Valid @RequestBody ClinicScheduleRequest request) {
        ClinicScheduleResponse response = clinicScheduleService.createSchedule(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get a clinic schedule by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClinicScheduleResponse> getScheduleById(@PathVariable Long id) {
        ClinicScheduleResponse response = clinicScheduleService.getScheduleById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get schedules by clinic ID
     */
    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<List<ClinicScheduleResponse>> getSchedulesByClinicId(@PathVariable Long clinicId) {
        List<ClinicScheduleResponse> response = clinicScheduleService.getSchedulesByClinicId(clinicId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get active schedules by clinic ID
     */
    @GetMapping("/clinic/{clinicId}/active")
    public ResponseEntity<List<ClinicScheduleResponse>> getActiveSchedulesByClinicId(@PathVariable Long clinicId) {
        List<ClinicScheduleResponse> response = clinicScheduleService.getActiveSchedulesByClinicId(clinicId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get schedule by clinic ID and day of week
     */
    @GetMapping("/clinic/{clinicId}/day/{dayOfWeek}")
    public ResponseEntity<ClinicScheduleResponse> getScheduleByClinicAndDay(
            @PathVariable Long clinicId,
            @PathVariable DayOfWeek dayOfWeek) {
        ClinicScheduleResponse response = clinicScheduleService.getScheduleByClinicAndDay(clinicId, dayOfWeek);
        return ResponseEntity.ok(response);
    }

    /**
     * Update a clinic schedule
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ClinicScheduleResponse> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ClinicScheduleRequest request) {
        ClinicScheduleResponse response = clinicScheduleService.updateSchedule(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Toggle schedule active status
     */
    @PutMapping("/{id}/toggle")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> toggleScheduleActive(
            @PathVariable Long id,
            @RequestParam Boolean isActive) {
        clinicScheduleService.toggleScheduleActive(id, isActive);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a clinic schedule
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        clinicScheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all schedules for a clinic
     */
    @DeleteMapping("/clinic/{clinicId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteAllSchedulesForClinic(@PathVariable Long clinicId) {
        clinicScheduleService.deleteAllSchedulesForClinic(clinicId);
        return ResponseEntity.noContent().build();
    }
} 