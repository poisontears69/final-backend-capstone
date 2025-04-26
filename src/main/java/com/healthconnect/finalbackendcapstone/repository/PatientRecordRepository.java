package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.PatientRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatientRecordRepository extends JpaRepository<PatientRecord, Long> {
    List<PatientRecord> findByPatientId(Long patientId);
    List<PatientRecord> findByDoctorId(Long doctorId);
}