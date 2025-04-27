package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.DoctorSocialLinkRequest;
import com.healthconnect.finalbackendcapstone.dto.DoctorSocialLinkResponse;

import java.util.List;

public interface DoctorSocialLinkService {
    DoctorSocialLinkResponse createSocialLink(DoctorSocialLinkRequest request);
    DoctorSocialLinkResponse getSocialLinkById(Long id);
    List<DoctorSocialLinkResponse> getSocialLinksByDoctorId(Long doctorId);
    DoctorSocialLinkResponse updateSocialLink(Long id, DoctorSocialLinkRequest request);
    void deleteSocialLink(Long id);
    void deleteAllSocialLinksForDoctor(Long doctorId);
} 