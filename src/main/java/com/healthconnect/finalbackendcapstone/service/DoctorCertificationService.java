package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.DoctorCertificationRequest;
import com.healthconnect.finalbackendcapstone.dto.DoctorCertificationResponse;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.DoctorCertification;
import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import com.healthconnect.finalbackendcapstone.repository.DoctorCertificationRepository;
import com.healthconnect.finalbackendcapstone.repository.DoctorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorCertificationService {
    private final DoctorCertificationRepository doctorCertificationRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    @Transactional
    public DoctorCertificationResponse createCertification(DoctorCertificationRequest request) {
        // Validate doctor exists
        DoctorProfile doctor = doctorProfileRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));
        
        // Create new certification
        DoctorCertification certification = new DoctorCertification();
        certification.setDoctor(doctor);
        certification.setCertificationType(request.getCertificationType());
        certification.setTitle(request.getTitle());
        
        DoctorCertification savedCertification = doctorCertificationRepository.save(certification);
        return mapToResponse(savedCertification);
    }
    
    @Transactional(readOnly = true)
    public DoctorCertificationResponse getCertificationById(Long id) {
        DoctorCertification certification = doctorCertificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor certification not found with id: " + id));
        return mapToResponse(certification);
    }
    
    @Transactional(readOnly = true)
    public List<DoctorCertificationResponse> getCertificationsByDoctorId(Long doctorId) {
        // Verify doctor exists
        if (!doctorProfileRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        
        List<DoctorCertification> certifications = doctorCertificationRepository.findByDoctorId(doctorId);
        return certifications.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<DoctorCertificationResponse> getCertificationsByDoctorIdAndType(
            Long doctorId, 
            DoctorCertification.CertificationType type) {
        // Verify doctor exists
        if (!doctorProfileRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        
        List<DoctorCertification> certifications = 
                doctorCertificationRepository.findByDoctorIdAndCertificationType(doctorId, type);
        return certifications.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional
    public DoctorCertificationResponse updateCertification(Long id, DoctorCertificationRequest request) {
        DoctorCertification certification = doctorCertificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor certification not found with id: " + id));
        
        // Check if doctor is changing
        if (!certification.getDoctor().getId().equals(request.getDoctorId())) {
            DoctorProfile doctor = doctorProfileRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));
            certification.setDoctor(doctor);
        }
        
        certification.setCertificationType(request.getCertificationType());
        certification.setTitle(request.getTitle());
        
        DoctorCertification updatedCertification = doctorCertificationRepository.save(certification);
        return mapToResponse(updatedCertification);
    }
    
    @Transactional
    public void deleteCertification(Long id) {
        if (!doctorCertificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor certification not found with id: " + id);
        }
        doctorCertificationRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteAllCertificationsForDoctor(Long doctorId) {
        if (!doctorProfileRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        doctorCertificationRepository.deleteByDoctorId(doctorId);
    }
    
    private DoctorCertificationResponse mapToResponse(DoctorCertification certification) {
        return DoctorCertificationResponse.builder()
                .id(certification.getId())
                .doctorId(certification.getDoctor().getId())
                .doctorName(certification.getDoctor().getUser().getFullName())
                .certificationType(certification.getCertificationType())
                .certificationTypeDisplay(
                        DoctorCertificationResponse.getCertificationTypeDisplay(certification.getCertificationType()))
                .title(certification.getTitle())
                .build();
    }
} 