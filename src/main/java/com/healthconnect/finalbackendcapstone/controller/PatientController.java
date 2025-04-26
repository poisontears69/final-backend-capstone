package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.model.Patient;
import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.service.PatientService;
import com.healthconnect.finalbackendcapstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserRepository userRepository;

    // POST endpoint to create a patient
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        User user = userRepository.findById(patient.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        patient.setUser(user);  // Link the patient with the user
        return patientService.savePatient(patient);  // Save the patient and return it
    }
}
