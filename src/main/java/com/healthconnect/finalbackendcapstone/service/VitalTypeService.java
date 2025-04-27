package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.VitalTypeRequest;
import com.healthconnect.finalbackendcapstone.dto.VitalTypeResponse;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.VitalType;
import com.healthconnect.finalbackendcapstone.repository.VitalTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VitalTypeService {
    private final VitalTypeRepository vitalTypeRepository;

    @Transactional
    public VitalTypeResponse createVitalType(VitalTypeRequest request) {
        // Check if vital type already exists
        if (vitalTypeRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalStateException("Vital type with name '" + request.getName() + "' already exists");
        }
        
        VitalType vitalType = new VitalType();
        vitalType.setName(request.getName());
        vitalType.setUnit(request.getUnit());
        vitalType.setDescription(request.getDescription());
        
        VitalType savedVitalType = vitalTypeRepository.save(vitalType);
        return mapToResponse(savedVitalType);
    }
    
    @Transactional(readOnly = true)
    public VitalTypeResponse getVitalTypeById(Integer id) {
        VitalType vitalType = vitalTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vital type not found with id: " + id));
        return mapToResponse(vitalType);
    }
    
    @Transactional(readOnly = true)
    public VitalTypeResponse getVitalTypeByName(String name) {
        VitalType vitalType = vitalTypeRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Vital type not found with name: " + name));
        return mapToResponse(vitalType);
    }
    
    @Transactional(readOnly = true)
    public List<VitalTypeResponse> getAllVitalTypes() {
        List<VitalType> vitalTypes = vitalTypeRepository.findAll();
        return vitalTypes.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<VitalTypeResponse> searchVitalTypes(String name) {
        List<VitalType> vitalTypes = vitalTypeRepository.findByNameContainingIgnoreCase(name);
        return vitalTypes.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional
    public VitalTypeResponse updateVitalType(Integer id, VitalTypeRequest request) {
        VitalType vitalType = vitalTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vital type not found with id: " + id));
        
        // Check for name duplicate if name is changing
        if (!vitalType.getName().equalsIgnoreCase(request.getName()) && 
                vitalTypeRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalStateException("Vital type with name '" + request.getName() + "' already exists");
        }
        
        vitalType.setName(request.getName());
        vitalType.setUnit(request.getUnit());
        vitalType.setDescription(request.getDescription());
        
        VitalType updatedVitalType = vitalTypeRepository.save(vitalType);
        return mapToResponse(updatedVitalType);
    }
    
    @Transactional
    public void deleteVitalType(Integer id) {
        if (!vitalTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vital type not found with id: " + id);
        }
        vitalTypeRepository.deleteById(id);
    }
    
    private VitalTypeResponse mapToResponse(VitalType vitalType) {
        return VitalTypeResponse.builder()
                .id(vitalType.getId())
                .name(vitalType.getName())
                .unit(vitalType.getUnit())
                .description(vitalType.getDescription())
                .build();
    }
} 