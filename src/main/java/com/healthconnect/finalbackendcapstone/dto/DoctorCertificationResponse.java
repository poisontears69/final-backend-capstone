package com.healthconnect.finalbackendcapstone.dto;

import com.healthconnect.finalbackendcapstone.model.DoctorCertification;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorCertificationResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private DoctorCertification.CertificationType certificationType;
    private String certificationTypeDisplay;
    private String title;

    // Helper method to get a display-friendly version of the certification type
    public static String getCertificationTypeDisplay(DoctorCertification.CertificationType type) {
        if (type == null) {
            return "";
        }
        
        switch (type) {
            case LOCAL_BOARD:
                return "Local Board";
            case INTERNATIONAL_BOARD:
                return "International Board";
            default:
                return type.name();
        }
    }
} 