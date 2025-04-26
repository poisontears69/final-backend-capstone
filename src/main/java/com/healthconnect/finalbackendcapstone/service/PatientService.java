package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.model.Patient;
import com.healthconnect.finalbackendcapstone.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    // Save a patient to the database
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }
}
