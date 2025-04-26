package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.model.PatientRecord;
import com.healthconnect.finalbackendcapstone.service.PatientRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class PatientRecordController {
    private final PatientRecordService recordService;

    @PostMapping
    public PatientRecord createRecord(@RequestBody PatientRecord record) {
        return recordService.createRecord(record);
    }

    @GetMapping("/patient/{patientId}")
    public List<PatientRecord> getByPatient(@PathVariable Long patientId) {
        return recordService.getRecordsByPatient(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<PatientRecord> getByDoctor(@PathVariable Long doctorId) {
        return recordService.getRecordsByDoctor(doctorId);
    }

    @DeleteMapping("/{id}")
    public void deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
    }
}