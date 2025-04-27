package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.Allergy;
import com.healthconnect.finalbackendcapstone.model.Allergy.SeverityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {
    List<Allergy> findByPatientRecordId(Long patientRecordId);
    List<Allergy> findByAllergenContainingIgnoreCase(String allergen);
    List<Allergy> findBySeverity(SeverityType severity);
    List<Allergy> findByPatientRecordIdAndSeverity(Long patientRecordId, SeverityType severity);
    List<Allergy> findByPatientRecordIdAndAllergenContainingIgnoreCase(Long patientRecordId, String allergen);
} 