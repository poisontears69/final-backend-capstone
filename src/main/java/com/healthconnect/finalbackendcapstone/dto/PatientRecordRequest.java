package com.healthconnect.finalbackendcapstone.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PatientRecordRequest {
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
} 