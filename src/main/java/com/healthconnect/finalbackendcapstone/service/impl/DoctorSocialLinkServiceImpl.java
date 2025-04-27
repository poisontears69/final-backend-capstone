package com.healthconnect.finalbackendcapstone.service.impl;

import com.healthconnect.finalbackendcapstone.dto.DoctorSocialLinkRequest;
import com.healthconnect.finalbackendcapstone.dto.DoctorSocialLinkResponse;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.DoctorSocialLink;
import com.healthconnect.finalbackendcapstone.repository.DoctorSocialLinkRepository;
import com.healthconnect.finalbackendcapstone.repository.DoctorProfileRepository;
import com.healthconnect.finalbackendcapstone.service.DoctorSocialLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorSocialLinkServiceImpl implements DoctorSocialLinkService {
    private final DoctorSocialLinkRepository doctorSocialLinkRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    @Override
    @Transactional
    public DoctorSocialLinkResponse createSocialLink(DoctorSocialLinkRequest request) {
        // Validate doctor exists
        if (!doctorProfileRepository.existsById(request.getDoctorId())) {
            throw new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId());
        }
        
        // Create new social link
        DoctorSocialLink socialLink = new DoctorSocialLink();
        socialLink.setDoctorId(request.getDoctorId());
        socialLink.setUrl(request.getUrl());
        
        DoctorSocialLink savedSocialLink = doctorSocialLinkRepository.save(socialLink);
        return mapToResponse(savedSocialLink);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DoctorSocialLinkResponse getSocialLinkById(Long id) {
        DoctorSocialLink socialLink = doctorSocialLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor social link not found with id: " + id));
        return mapToResponse(socialLink);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DoctorSocialLinkResponse> getSocialLinksByDoctorId(Long doctorId) {
        // Verify doctor exists
        if (!doctorProfileRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        
        List<DoctorSocialLink> socialLinks = doctorSocialLinkRepository.findByDoctorId(doctorId);
        return socialLinks.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public DoctorSocialLinkResponse updateSocialLink(Long id, DoctorSocialLinkRequest request) {
        DoctorSocialLink socialLink = doctorSocialLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor social link not found with id: " + id));
        
        // Check if doctor is changing and exists
        if (!socialLink.getDoctorId().equals(request.getDoctorId())) {
            if (!doctorProfileRepository.existsById(request.getDoctorId())) {
                throw new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId());
            }
            socialLink.setDoctorId(request.getDoctorId());
        }
        
        socialLink.setUrl(request.getUrl());
        
        DoctorSocialLink updatedSocialLink = doctorSocialLinkRepository.save(socialLink);
        return mapToResponse(updatedSocialLink);
    }
    
    @Override
    @Transactional
    public void deleteSocialLink(Long id) {
        if (!doctorSocialLinkRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor social link not found with id: " + id);
        }
        doctorSocialLinkRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public void deleteAllSocialLinksForDoctor(Long doctorId) {
        if (!doctorProfileRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        doctorSocialLinkRepository.deleteByDoctorId(doctorId);
    }
    
    private DoctorSocialLinkResponse mapToResponse(DoctorSocialLink socialLink) {
        return DoctorSocialLinkResponse.builder()
                .id(socialLink.getId())
                .doctorId(socialLink.getDoctorId())
                .url(socialLink.getUrl())
                .build();
    }
} 