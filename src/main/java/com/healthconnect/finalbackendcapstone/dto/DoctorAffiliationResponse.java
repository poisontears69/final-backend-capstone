package com.healthconnect.finalbackendcapstone.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorAffiliationResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private String institutionName;
    
    // Additional doctor details
    private String doctorSpecialty;
    private String doctorEmail;
} 