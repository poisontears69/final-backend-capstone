package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.DoctorEducationRequest;
import com.healthconnect.finalbackendcapstone.dto.DoctorEducationResponse;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.DoctorEducation;
import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import com.healthconnect.finalbackendcapstone.repository.DoctorEducationRepository;
import com.healthconnect.finalbackendcapstone.repository.DoctorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorEducationService {
    private final DoctorEducationRepository doctorEducationRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    @Transactional
    public DoctorEducationResponse createEducation(DoctorEducationRequest request) {
        // Validate doctor exists
        DoctorProfile doctor = doctorProfileRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));
        
        // Create new education record
        DoctorEducation education = new DoctorEducation();
        education.setDoctor(doctor);
        education.setEducationType(request.getEducationType());
        education.setInstitutionName(request.getInstitutionName());
        education.setYearCompleted(request.getYearCompleted());
        education.setDescription(request.getDescription());
        
        DoctorEducation savedEducation = doctorEducationRepository.save(education);
        return mapToResponse(savedEducation);
    }
    
    @Transactional(readOnly = true)
    public DoctorEducationResponse getEducationById(Long id) {
        DoctorEducation education = doctorEducationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor education not found with id: " + id));
        return mapToResponse(education);
    }
    
    @Transactional(readOnly = true)
    public List<DoctorEducationResponse> getEducationByDoctorId(Long doctorId) {
        // Verify doctor exists
        if (!doctorProfileRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        
        List<DoctorEducation> educations = doctorEducationRepository.findByDoctorId(doctorId);
        return educations.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<DoctorEducationResponse> getEducationByDoctorIdAndType(Long doctorId, DoctorEducation.EducationType type) {
        // Verify doctor exists
        if (!doctorProfileRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        
        List<DoctorEducation> educations = doctorEducationRepository.findByDoctorIdAndEducationType(doctorId, type);
        return educations.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional
    public DoctorEducationResponse updateEducation(Long id, DoctorEducationRequest request) {
        DoctorEducation education = doctorEducationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor education not found with id: " + id));
        
        // Check if doctor is changing
        if (!education.getDoctor().getId().equals(request.getDoctorId())) {
            DoctorProfile doctor = doctorProfileRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));
            education.setDoctor(doctor);
        }
        
        education.setEducationType(request.getEducationType());
        education.setInstitutionName(request.getInstitutionName());
        education.setYearCompleted(request.getYearCompleted());
        education.setDescription(request.getDescription());
        
        DoctorEducation updatedEducation = doctorEducationRepository.save(education);
        return mapToResponse(updatedEducation);
    }
    
    @Transactional
    public void deleteEducation(Long id) {
        if (!doctorEducationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor education not found with id: " + id);
        }
        doctorEducationRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteAllEducationForDoctor(Long doctorId) {
        if (!doctorProfileRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        doctorEducationRepository.deleteByDoctorId(doctorId);
    }
    
    private DoctorEducationResponse mapToResponse(DoctorEducation education) {
        return DoctorEducationResponse.builder()
                .id(education.getId())
                .doctorId(education.getDoctor().getId())
                .doctorName(education.getDoctor().getUser().getFullName())
                .educationType(education.getEducationType())
                .educationTypeDisplay(DoctorEducationResponse.getEducationTypeDisplay(education.getEducationType()))
                .institutionName(education.getInstitutionName())
                .yearCompleted(education.getYearCompleted())
                .description(education.getDescription())
                .build();
    }
} 