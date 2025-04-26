package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.model.Medication;
import com.healthconnect.finalbackendcapstone.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping
    public Medication createMedication(@RequestBody Medication medication) {
        return medicationService.createMedication(medication);
    }

    @GetMapping("/record/{patientRecordId}")
    public List<Medication> getByPatientRecord(@PathVariable Long patientRecordId) {
        return medicationService.getMedicationsByPatientRecord(patientRecordId);
    }

    @GetMapping("/date-range")
    public List<Medication> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return medicationService.getMedicationsByDateRange(startDate, endDate);
    }

    @GetMapping("/search")
    public List<Medication> searchByName(@RequestParam String name) {
        return medicationService.searchMedicationsByName(name);
    }

    @DeleteMapping("/{id}")
    public void deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
    }
}