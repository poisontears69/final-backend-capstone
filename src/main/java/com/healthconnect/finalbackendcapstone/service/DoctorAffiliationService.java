package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.DoctorAffiliationRequest;
import com.healthconnect.finalbackendcapstone.dto.DoctorAffiliationResponse;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.DoctorAffiliation;
import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import com.healthconnect.finalbackendcapstone.repository.DoctorAffiliationRepository;
import com.healthconnect.finalbackendcapstone.repository.DoctorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorAffiliationService {
    private final DoctorAffiliationRepository doctorAffiliationRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    @Transactional
    public DoctorAffiliationResponse createAffiliation(DoctorAffiliationRequest request) {
        // Validate doctor exists
        DoctorProfile doctor = doctorProfileRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));
        
        // Create new affiliation
        DoctorAffiliation affiliation = new DoctorAffiliation();
        affiliation.setDoctor(doctor);
        affiliation.setInstitutionName(request.getInstitutionName());
        
        DoctorAffiliation savedAffiliation = doctorAffiliationRepository.save(affiliation);
        return mapToResponse(savedAffiliation);
    }
    
    @Transactional(readOnly = true)
    public DoctorAffiliationResponse getAffiliationById(Long id) {
        DoctorAffiliation affiliation = doctorAffiliationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor affiliation not found with id: " + id));
        return mapToResponse(affiliation);
    }
    
    @Transactional(readOnly = true)
    public List<DoctorAffiliationResponse> getAffiliationsByDoctorId(Long doctorId) {
        // Verify doctor exists
        if (!doctorProfileRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        
        List<DoctorAffiliation> affiliations = doctorAffiliationRepository.findByDoctorId(doctorId);
        return affiliations.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<DoctorAffiliationResponse> searchAffiliationsByInstitutionName(String institutionName) {
        List<DoctorAffiliation> affiliations = doctorAffiliationRepository
                .findByInstitutionNameContainingIgnoreCase(institutionName);
        return affiliations.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<DoctorAffiliationResponse> searchDoctorAffiliationsByInstitutionName(
            Long doctorId, String institutionName) {
        // Verify doctor exists
        if (!doctorProfileRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        
        List<DoctorAffiliation> affiliations = doctorAffiliationRepository
                .findByDoctorIdAndInstitutionNameContainingIgnoreCase(doctorId, institutionName);
        return affiliations.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional
    public DoctorAffiliationResponse updateAffiliation(Long id, DoctorAffiliationRequest request) {
        DoctorAffiliation affiliation = doctorAffiliationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor affiliation not found with id: " + id));
        
        // Check if doctor is changing
        if (!affiliation.getDoctor().getId().equals(request.getDoctorId())) {
            DoctorProfile doctor = doctorProfileRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));
            affiliation.setDoctor(doctor);
        }
        
        affiliation.setInstitutionName(request.getInstitutionName());
        
        DoctorAffiliation updatedAffiliation = doctorAffiliationRepository.save(affiliation);
        return mapToResponse(updatedAffiliation);
    }
    
    @Transactional
    public void deleteAffiliation(Long id) {
        if (!doctorAffiliationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor affiliation not found with id: " + id);
        }
        doctorAffiliationRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteAllAffiliationsForDoctor(Long doctorId) {
        if (!doctorProfileRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        doctorAffiliationRepository.deleteByDoctorId(doctorId);
    }
    
    private DoctorAffiliationResponse mapToResponse(DoctorAffiliation affiliation) {
        return DoctorAffiliationResponse.builder()
                .id(affiliation.getId())
                .doctorId(affiliation.getDoctor().getId())
                .doctorName(affiliation.getDoctor().getUser().getFullName())
                .institutionName(affiliation.getInstitutionName())
                .doctorSpecialty(affiliation.getDoctor().getTitle())
                .doctorEmail(affiliation.getDoctor().getEmail())
                .build();
    }
} 