package com.healthconnect.finalbackendcapstone.service.impl;

import com.healthconnect.finalbackendcapstone.dto.HealthRecordPhotoDTO;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.HealthRecord;
import com.healthconnect.finalbackendcapstone.model.HealthRecordPhoto;
import com.healthconnect.finalbackendcapstone.repository.HealthRecordPhotoRepository;
import com.healthconnect.finalbackendcapstone.repository.HealthRecordRepository;
import com.healthconnect.finalbackendcapstone.service.HealthRecordPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HealthRecordPhotoServiceImpl implements HealthRecordPhotoService {

    private final HealthRecordPhotoRepository healthRecordPhotoRepository;
    private final HealthRecordRepository healthRecordRepository;

    @Autowired
    public HealthRecordPhotoServiceImpl(HealthRecordPhotoRepository healthRecordPhotoRepository,
                                       HealthRecordRepository healthRecordRepository) {
        this.healthRecordPhotoRepository = healthRecordPhotoRepository;
        this.healthRecordRepository = healthRecordRepository;
    }

    @Override
    public HealthRecordPhotoDTO createHealthRecordPhoto(HealthRecordPhotoDTO healthRecordPhotoDTO) {
        HealthRecordPhoto healthRecordPhoto = mapToEntity(healthRecordPhotoDTO);
        HealthRecordPhoto savedHealthRecordPhoto = healthRecordPhotoRepository.save(healthRecordPhoto);
        return mapToDTO(savedHealthRecordPhoto);
    }

    @Override
    public HealthRecordPhotoDTO getHealthRecordPhotoById(Long id) {
        HealthRecordPhoto healthRecordPhoto = healthRecordPhotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HealthRecordPhoto", "id", id));
        return mapToDTO(healthRecordPhoto);
    }

    @Override
    public List<HealthRecordPhotoDTO> getHealthRecordPhotosByHealthRecordId(Long healthRecordId) {
        List<HealthRecordPhoto> healthRecordPhotos = healthRecordPhotoRepository.findByHealthRecordId(healthRecordId);
        return healthRecordPhotos.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HealthRecordPhotoDTO updateHealthRecordPhoto(Long id, HealthRecordPhotoDTO healthRecordPhotoDTO) {
        HealthRecordPhoto healthRecordPhoto = healthRecordPhotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HealthRecordPhoto", "id", id));
        
        healthRecordPhoto.setFileUrl(healthRecordPhotoDTO.getFileUrl());
        healthRecordPhoto.setFileName(healthRecordPhotoDTO.getFileName());
        healthRecordPhoto.setFileType(healthRecordPhotoDTO.getFileType());
        
        // Only update health record reference if provided
        if (healthRecordPhotoDTO.getHealthRecordId() != null) {
            HealthRecord healthRecord = healthRecordRepository.findById(healthRecordPhotoDTO.getHealthRecordId())
                    .orElseThrow(() -> new ResourceNotFoundException("HealthRecord", "id", healthRecordPhotoDTO.getHealthRecordId()));
            healthRecordPhoto.setHealthRecord(healthRecord);
        }
        
        HealthRecordPhoto updatedHealthRecordPhoto = healthRecordPhotoRepository.save(healthRecordPhoto);
        return mapToDTO(updatedHealthRecordPhoto);
    }

    @Override
    public void deleteHealthRecordPhoto(Long id) {
        HealthRecordPhoto healthRecordPhoto = healthRecordPhotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HealthRecordPhoto", "id", id));
        healthRecordPhotoRepository.delete(healthRecordPhoto);
    }

    @Override
    @Transactional
    public void deleteAllHealthRecordPhotosByHealthRecordId(Long healthRecordId) {
        // Check if health record exists
        if (!healthRecordRepository.existsById(healthRecordId)) {
            throw new ResourceNotFoundException("HealthRecord", "id", healthRecordId);
        }
        healthRecordPhotoRepository.deleteByHealthRecordId(healthRecordId);
    }

    private HealthRecordPhoto mapToEntity(HealthRecordPhotoDTO healthRecordPhotoDTO) {
        HealthRecordPhoto healthRecordPhoto = new HealthRecordPhoto();
        
        if (healthRecordPhotoDTO.getId() != null) {
            healthRecordPhoto.setId(healthRecordPhotoDTO.getId());
        }
        
        healthRecordPhoto.setFileUrl(healthRecordPhotoDTO.getFileUrl());
        healthRecordPhoto.setFileName(healthRecordPhotoDTO.getFileName());
        healthRecordPhoto.setFileType(healthRecordPhotoDTO.getFileType());
        
        if (healthRecordPhotoDTO.getHealthRecordId() != null) {
            HealthRecord healthRecord = healthRecordRepository.findById(healthRecordPhotoDTO.getHealthRecordId())
                    .orElseThrow(() -> new ResourceNotFoundException("HealthRecord", "id", healthRecordPhotoDTO.getHealthRecordId()));
            healthRecordPhoto.setHealthRecord(healthRecord);
        }
        
        return healthRecordPhoto;
    }

    private HealthRecordPhotoDTO mapToDTO(HealthRecordPhoto healthRecordPhoto) {
        HealthRecordPhotoDTO healthRecordPhotoDTO = new HealthRecordPhotoDTO();
        
        healthRecordPhotoDTO.setId(healthRecordPhoto.getId());
        healthRecordPhotoDTO.setFileUrl(healthRecordPhoto.getFileUrl());
        healthRecordPhotoDTO.setFileName(healthRecordPhoto.getFileName());
        healthRecordPhotoDTO.setFileType(healthRecordPhoto.getFileType());
        healthRecordPhotoDTO.setCreatedAt(healthRecordPhoto.getCreatedAt());
        
        if (healthRecordPhoto.getHealthRecord() != null) {
            healthRecordPhotoDTO.setHealthRecordId(healthRecordPhoto.getHealthRecord().getId());
        }
        
        return healthRecordPhotoDTO;
    }
} 