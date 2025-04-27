package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.ImmunizationDTO;

import java.util.List;

public interface ImmunizationService {
    ImmunizationDTO createImmunization(ImmunizationDTO immunizationDTO);
    ImmunizationDTO getImmunizationById(Long id);
    List<ImmunizationDTO> getImmunizationsByPatientRecordId(Long patientRecordId);
    ImmunizationDTO updateImmunization(Long id, ImmunizationDTO immunizationDTO);
    void deleteImmunization(Long id);
} 