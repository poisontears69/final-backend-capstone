package com.healthconnect.finalbackendcapstone.service.impl;

import com.healthconnect.finalbackendcapstone.dto.ImmunizationDTO;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.Immunization;
import com.healthconnect.finalbackendcapstone.model.PatientRecord;
import com.healthconnect.finalbackendcapstone.repository.ImmunizationRepository;
import com.healthconnect.finalbackendcapstone.repository.PatientRecordRepository;
import com.healthconnect.finalbackendcapstone.service.ImmunizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImmunizationServiceImpl implements ImmunizationService {

    private final ImmunizationRepository immunizationRepository;
    private final PatientRecordRepository patientRecordRepository;

    @Autowired
    public ImmunizationServiceImpl(ImmunizationRepository immunizationRepository, 
                                  PatientRecordRepository patientRecordRepository) {
        this.immunizationRepository = immunizationRepository;
        this.patientRecordRepository = patientRecordRepository;
    }

    @Override
    public ImmunizationDTO createImmunization(ImmunizationDTO immunizationDTO) {
        Immunization immunization = mapToEntity(immunizationDTO);
        Immunization savedImmunization = immunizationRepository.save(immunization);
        return mapToDTO(savedImmunization);
    }

    @Override
    public ImmunizationDTO getImmunizationById(Long id) {
        Immunization immunization = immunizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Immunization", "id", id));
        return mapToDTO(immunization);
    }

    @Override
    public List<ImmunizationDTO> getImmunizationsByPatientRecordId(Long patientRecordId) {
        List<Immunization> immunizations = immunizationRepository.findByPatientRecordId(patientRecordId);
        return immunizations.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ImmunizationDTO updateImmunization(Long id, ImmunizationDTO immunizationDTO) {
        Immunization immunization = immunizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Immunization", "id", id));
        
        immunization.setVaccineName(immunizationDTO.getVaccineName());
        immunization.setDateAdministered(immunizationDTO.getDateAdministered());
        
        Immunization updatedImmunization = immunizationRepository.save(immunization);
        return mapToDTO(updatedImmunization);
    }

    @Override
    public void deleteImmunization(Long id) {
        Immunization immunization = immunizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Immunization", "id", id));
        immunizationRepository.delete(immunization);
    }

    private Immunization mapToEntity(ImmunizationDTO immunizationDTO) {
        Immunization immunization = new Immunization();
        
        if (immunizationDTO.getId() != null) {
            immunization.setId(immunizationDTO.getId());
        }
        
        immunization.setVaccineName(immunizationDTO.getVaccineName());
        immunization.setDateAdministered(immunizationDTO.getDateAdministered());
        
        if (immunizationDTO.getPatientRecordId() != null) {
            PatientRecord patientRecord = patientRecordRepository.findById(immunizationDTO.getPatientRecordId())
                    .orElseThrow(() -> new ResourceNotFoundException("PatientRecord", "id", immunizationDTO.getPatientRecordId()));
            immunization.setPatientRecord(patientRecord);
        }
        
        return immunization;
    }

    private ImmunizationDTO mapToDTO(Immunization immunization) {
        ImmunizationDTO immunizationDTO = new ImmunizationDTO();
        
        immunizationDTO.setId(immunization.getId());
        immunizationDTO.setVaccineName(immunization.getVaccineName());
        immunizationDTO.setDateAdministered(immunization.getDateAdministered());
        
        if (immunization.getPatientRecord() != null) {
            immunizationDTO.setPatientRecordId(immunization.getPatientRecord().getId());
        }
        
        immunizationDTO.setCreatedAt(immunization.getCreatedAt());
        
        return immunizationDTO;
    }
} 