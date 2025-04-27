package com.healthconnect.finalbackendcapstone.service.impl;

import com.healthconnect.finalbackendcapstone.dto.DoctorSocialLinkDTO;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import com.healthconnect.finalbackendcapstone.model.DoctorSocialLink;
import com.healthconnect.finalbackendcapstone.repository.DoctorProfileRepository;
import com.healthconnect.finalbackendcapstone.repository.DoctorSocialLinkRepository;
import com.healthconnect.finalbackendcapstone.service.DoctorSocialLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorSocialLinkServiceImpl implements DoctorSocialLinkService {

    private final DoctorSocialLinkRepository doctorSocialLinkRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    @Autowired
    public DoctorSocialLinkServiceImpl(DoctorSocialLinkRepository doctorSocialLinkRepository,
                                       DoctorProfileRepository doctorProfileRepository) {
        this.doctorSocialLinkRepository = doctorSocialLinkRepository;
        this.doctorProfileRepository = doctorProfileRepository;
    }

    @Override
    public DoctorSocialLinkDTO createDoctorSocialLink(DoctorSocialLinkDTO doctorSocialLinkDTO) {
        DoctorSocialLink doctorSocialLink = mapToEntity(doctorSocialLinkDTO);
        DoctorSocialLink savedDoctorSocialLink = doctorSocialLinkRepository.save(doctorSocialLink);
        return mapToDTO(savedDoctorSocialLink);
    }

    @Override
    public DoctorSocialLinkDTO getDoctorSocialLinkById(Long id) {
        DoctorSocialLink doctorSocialLink = doctorSocialLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DoctorSocialLink", "id", id));
        return mapToDTO(doctorSocialLink);
    }

    @Override
    public List<DoctorSocialLinkDTO> getDoctorSocialLinksByDoctorId(Long doctorId) {
        List<DoctorSocialLink> doctorSocialLinks = doctorSocialLinkRepository.findByDoctorId(doctorId);
        return doctorSocialLinks.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorSocialLinkDTO updateDoctorSocialLink(Long id, DoctorSocialLinkDTO doctorSocialLinkDTO) {
        DoctorSocialLink doctorSocialLink = doctorSocialLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DoctorSocialLink", "id", id));
        
        doctorSocialLink.setUrl(doctorSocialLinkDTO.getUrl());
        
        // Only update doctor reference if provided
        if (doctorSocialLinkDTO.getDoctorId() != null) {
            DoctorProfile doctorProfile = doctorProfileRepository.findById(doctorSocialLinkDTO.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("DoctorProfile", "id", doctorSocialLinkDTO.getDoctorId()));
            doctorSocialLink.setDoctor(doctorProfile);
        }
        
        DoctorSocialLink updatedDoctorSocialLink = doctorSocialLinkRepository.save(doctorSocialLink);
        return mapToDTO(updatedDoctorSocialLink);
    }

    @Override
    public void deleteDoctorSocialLink(Long id) {
        DoctorSocialLink doctorSocialLink = doctorSocialLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DoctorSocialLink", "id", id));
        doctorSocialLinkRepository.delete(doctorSocialLink);
    }

    @Override
    @Transactional
    public void deleteAllDoctorSocialLinksByDoctorId(Long doctorId) {
        // Check if doctor exists
        if (!doctorProfileRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("DoctorProfile", "id", doctorId);
        }
        doctorSocialLinkRepository.deleteByDoctorId(doctorId);
    }

    private DoctorSocialLink mapToEntity(DoctorSocialLinkDTO doctorSocialLinkDTO) {
        DoctorSocialLink doctorSocialLink = new DoctorSocialLink();
        
        if (doctorSocialLinkDTO.getId() != null) {
            doctorSocialLink.setId(doctorSocialLinkDTO.getId());
        }
        
        doctorSocialLink.setUrl(doctorSocialLinkDTO.getUrl());
        
        if (doctorSocialLinkDTO.getDoctorId() != null) {
            DoctorProfile doctorProfile = doctorProfileRepository.findById(doctorSocialLinkDTO.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("DoctorProfile", "id", doctorSocialLinkDTO.getDoctorId()));
            doctorSocialLink.setDoctor(doctorProfile);
        }
        
        return doctorSocialLink;
    }

    private DoctorSocialLinkDTO mapToDTO(DoctorSocialLink doctorSocialLink) {
        DoctorSocialLinkDTO doctorSocialLinkDTO = new DoctorSocialLinkDTO();
        
        doctorSocialLinkDTO.setId(doctorSocialLink.getId());
        doctorSocialLinkDTO.setUrl(doctorSocialLink.getUrl());
        
        if (doctorSocialLink.getDoctor() != null) {
            doctorSocialLinkDTO.setDoctorId(doctorSocialLink.getDoctor().getId());
        }
        
        return doctorSocialLinkDTO;
    }
} 