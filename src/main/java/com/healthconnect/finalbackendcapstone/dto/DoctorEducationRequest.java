package com.healthconnect.finalbackendcapstone.dto;

import com.healthconnect.finalbackendcapstone.model.DoctorEducation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.Year;

@Data
public class DoctorEducationRequest {
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    
    @NotNull(message = "Education type is required")
    private DoctorEducation.EducationType educationType;
    
    @NotBlank(message = "Institution name is required")
    private String institutionName;
    
    @Past(message = "Year completed must be in the past")
    private Year yearCompleted;
    
    private String description;
} 