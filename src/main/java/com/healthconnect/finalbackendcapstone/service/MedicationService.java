package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.model.Medication;
import com.healthconnect.finalbackendcapstone.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;

    @Transactional
    public Medication createMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    @Transactional(readOnly = true)
    public List<Medication> getMedicationsByPatientRecord(Long patientRecordId) {
        return medicationRepository.findByPatientRecordId(patientRecordId);
    }

    @Transactional(readOnly = true)
    public List<Medication> getMedicationsByDateRange(LocalDate startDate, LocalDate endDate) {
        return medicationRepository.findMedicationsByDateRange(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Medication> searchMedicationsByName(String name) {
        return medicationRepository.findByMedicationNameContainingIgnoreCase(name);
    }

    @Transactional
    public void deleteMedication(Long id) {
        medicationRepository.deleteById(id);
    }
}