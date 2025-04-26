package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}
