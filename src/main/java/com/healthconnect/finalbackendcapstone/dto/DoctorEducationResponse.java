package com.healthconnect.finalbackendcapstone.dto;

import com.healthconnect.finalbackendcapstone.model.DoctorEducation;
import lombok.Builder;
import lombok.Data;

import java.time.Year;

@Data
@Builder
public class DoctorEducationResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private DoctorEducation.EducationType educationType;
    private String educationTypeDisplay;
    private String institutionName;
    private Year yearCompleted;
    private String description;

    // Helper method to get a display-friendly version of the education type
    public static String getEducationTypeDisplay(DoctorEducation.EducationType type) {
        if (type == null) {
            return "";
        }
        
        switch (type) {
            case MEDICAL_SCHOOL:
                return "Medical School";
            case RESIDENCY:
                return "Residency";
            case FELLOWSHIP:
                return "Fellowship";
            default:
                return type.name();
        }
    }
} 