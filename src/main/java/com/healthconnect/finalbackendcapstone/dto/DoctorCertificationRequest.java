package com.healthconnect.finalbackendcapstone.dto;

import com.healthconnect.finalbackendcapstone.model.DoctorCertification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DoctorCertificationRequest {
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    
    @NotNull(message = "Certification type is required")
    private DoctorCertification.CertificationType certificationType;
    
    @NotBlank(message = "Title is required")
    private String title;
} 