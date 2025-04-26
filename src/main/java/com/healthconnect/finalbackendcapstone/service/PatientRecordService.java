package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.model.PatientRecord;
import com.healthconnect.finalbackendcapstone.repository.PatientRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientRecordService {
    private final PatientRecordRepository patientRecordRepository;

    public PatientRecord createRecord(PatientRecord record) {
        return patientRecordRepository.save(record);
    }

    public List<PatientRecord> getRecordsByPatient(Long patientId) {
        return patientRecordRepository.findByPatientId(patientId);
    }

    public List<PatientRecord> getRecordsByDoctor(Long doctorId) {
        return patientRecordRepository.findByDoctorId(doctorId);
    }

    public void deleteRecord(Long id) {
        patientRecordRepository.deleteById(id);
    }
}