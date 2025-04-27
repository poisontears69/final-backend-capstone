package com.healthconnect.finalbackendcapstone.service.impl;

import com.healthconnect.finalbackendcapstone.dto.AllergyDTO;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.Allergy;
import com.healthconnect.finalbackendcapstone.model.Allergy.SeverityType;
import com.healthconnect.finalbackendcapstone.model.PatientRecord;
import com.healthconnect.finalbackendcapstone.repository.AllergyRepository;
import com.healthconnect.finalbackendcapstone.repository.PatientRecordRepository;
import com.healthconnect.finalbackendcapstone.service.AllergyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllergyServiceImpl implements AllergyService {

    private final AllergyRepository allergyRepository;
    private final PatientRecordRepository patientRecordRepository;

    @Autowired
    public AllergyServiceImpl(AllergyRepository allergyRepository,
                             PatientRecordRepository patientRecordRepository) {
        this.allergyRepository = allergyRepository;
        this.patientRecordRepository = patientRecordRepository;
    }

    @Override
    public AllergyDTO createAllergy(AllergyDTO allergyDTO) {
        Allergy allergy = mapToEntity(allergyDTO);
        Allergy savedAllergy = allergyRepository.save(allergy);
        return mapToDTO(savedAllergy);
    }

    @Override
    public AllergyDTO getAllergyById(Long id) {
        Allergy allergy = allergyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy", "id", id));
        return mapToDTO(allergy);
    }

    @Override
    public List<AllergyDTO> getAllergiesByPatientRecordId(Long patientRecordId) {
        List<Allergy> allergies = allergyRepository.findByPatientRecordId(patientRecordId);
        return allergies.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AllergyDTO> getAllergiesByAllergen(String allergen) {
        List<Allergy> allergies = allergyRepository.findByAllergenContainingIgnoreCase(allergen);
        return allergies.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AllergyDTO> getAllergiesBySeverity(SeverityType severity) {
        List<Allergy> allergies = allergyRepository.findBySeverity(severity);
        return allergies.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AllergyDTO> getAllergiesByPatientRecordIdAndSeverity(Long patientRecordId, SeverityType severity) {
        List<Allergy> allergies = allergyRepository.findByPatientRecordIdAndSeverity(patientRecordId, severity);
        return allergies.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AllergyDTO> getAllergiesByPatientRecordIdAndAllergen(Long patientRecordId, String allergen) {
        List<Allergy> allergies = allergyRepository.findByPatientRecordIdAndAllergenContainingIgnoreCase(patientRecordId, allergen);
        return allergies.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AllergyDTO updateAllergy(Long id, AllergyDTO allergyDTO) {
        Allergy allergy = allergyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy", "id", id));
        
        allergy.setAllergen(allergyDTO.getAllergen());
        allergy.setSeverity(allergyDTO.getSeverity());
        allergy.setReaction(allergyDTO.getReaction());
        
        Allergy updatedAllergy = allergyRepository.save(allergy);
        return mapToDTO(updatedAllergy);
    }

    @Override
    public void deleteAllergy(Long id) {
        Allergy allergy = allergyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy", "id", id));
        allergyRepository.delete(allergy);
    }

    private Allergy mapToEntity(AllergyDTO allergyDTO) {
        Allergy allergy = new Allergy();
        
        if (allergyDTO.getId() != null) {
            allergy.setId(allergyDTO.getId());
        }
        
        allergy.setAllergen(allergyDTO.getAllergen());
        allergy.setSeverity(allergyDTO.getSeverity());
        allergy.setReaction(allergyDTO.getReaction());
        
        if (allergyDTO.getPatientRecordId() != null) {
            PatientRecord patientRecord = patientRecordRepository.findById(allergyDTO.getPatientRecordId())
                    .orElseThrow(() -> new ResourceNotFoundException("PatientRecord", "id", allergyDTO.getPatientRecordId()));
            allergy.setPatientRecord(patientRecord);
        }
        
        return allergy;
    }

    private AllergyDTO mapToDTO(Allergy allergy) {
        AllergyDTO allergyDTO = new AllergyDTO();
        
        allergyDTO.setId(allergy.getId());
        allergyDTO.setAllergen(allergy.getAllergen());
        allergyDTO.setSeverity(allergy.getSeverity());
        allergyDTO.setReaction(allergy.getReaction());
        allergyDTO.setCreatedAt(allergy.getCreatedAt());
        
        if (allergy.getPatientRecord() != null) {
            allergyDTO.setPatientRecordId(allergy.getPatientRecord().getId());
        }
        
        return allergyDTO;
    }
} 