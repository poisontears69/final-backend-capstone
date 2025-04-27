package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.DoctorSocialLinkDTO;

import java.util.List;

public interface DoctorSocialLinkService {
    DoctorSocialLinkDTO createDoctorSocialLink(DoctorSocialLinkDTO doctorSocialLinkDTO);
    DoctorSocialLinkDTO getDoctorSocialLinkById(Long id);
    List<DoctorSocialLinkDTO> getDoctorSocialLinksByDoctorId(Long doctorId);
    DoctorSocialLinkDTO updateDoctorSocialLink(Long id, DoctorSocialLinkDTO doctorSocialLinkDTO);
    void deleteDoctorSocialLink(Long id);
    void deleteAllDoctorSocialLinksByDoctorId(Long doctorId);
} 