package com.healthconnect.finalbackendcapstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healthconnect.finalbackendcapstone.model.Clinic;
import com.healthconnect.finalbackendcapstone.service.ClinicService;

import java.util.List;

@RestController
@RequestMapping("/api/clinics")
public class ClinicController {

    @Autowired
    private ClinicService clinicService;

    // Create Clinic
    @PostMapping("/doctor/{doctorId}")
    public ResponseEntity<Clinic> createClinic(@PathVariable Long doctorId, @RequestBody Clinic clinic) {
        Clinic created = clinicService.createClinic(doctorId, clinic);
        return ResponseEntity.ok(created);
    }

    // Get Clinics by Doctor ID
    @GetMapping("/doctor/{doctorId}")
    public List<Clinic> getClinicsByDoctor(@PathVariable Long doctorId) {
        return clinicService.getClinicsByDoctor(doctorId);
    }

    // Update Clinic
    @PutMapping("/{id}")
    public Clinic updateClinic(@PathVariable Long id, @RequestBody Clinic clinic) {
        return clinicService.updateClinic(id, clinic);
    }

    // Delete Clinic
    @DeleteMapping("/{id}")
    public void deleteClinic(@PathVariable Long id) {
        clinicService.deleteClinic(id);
    }
}
