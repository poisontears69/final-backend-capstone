package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.AllergyDTO;
import com.healthconnect.finalbackendcapstone.model.Allergy.SeverityType;

import java.util.List;

public interface AllergyService {
    AllergyDTO createAllergy(AllergyDTO allergyDTO);
    AllergyDTO getAllergyById(Long id);
    List<AllergyDTO> getAllergiesByPatientRecordId(Long patientRecordId);
    List<AllergyDTO> getAllergiesByAllergen(String allergen);
    List<AllergyDTO> getAllergiesBySeverity(SeverityType severity);
    List<AllergyDTO> getAllergiesByPatientRecordIdAndSeverity(Long patientRecordId, SeverityType severity);
    List<AllergyDTO> getAllergiesByPatientRecordIdAndAllergen(Long patientRecordId, String allergen);
    AllergyDTO updateAllergy(Long id, AllergyDTO allergyDTO);
    void deleteAllergy(Long id);
} 