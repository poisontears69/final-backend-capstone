package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.HealthRecordPhotoDTO;

import java.util.List;

public interface HealthRecordPhotoService {
    HealthRecordPhotoDTO createHealthRecordPhoto(HealthRecordPhotoDTO healthRecordPhotoDTO);
    HealthRecordPhotoDTO getHealthRecordPhotoById(Long id);
    List<HealthRecordPhotoDTO> getHealthRecordPhotosByHealthRecordId(Long healthRecordId);
    HealthRecordPhotoDTO updateHealthRecordPhoto(Long id, HealthRecordPhotoDTO healthRecordPhotoDTO);
    void deleteHealthRecordPhoto(Long id);
    void deleteAllHealthRecordPhotosByHealthRecordId(Long healthRecordId);
} 