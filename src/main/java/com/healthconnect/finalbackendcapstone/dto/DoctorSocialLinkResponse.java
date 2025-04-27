package com.healthconnect.finalbackendcapstone.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorSocialLinkResponse {
    private Long id;
    private Long doctorId;
    private String url;
} 