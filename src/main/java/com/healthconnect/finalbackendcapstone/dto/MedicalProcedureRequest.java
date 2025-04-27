package com.healthconnect.finalbackendcapstone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicalProcedureRequest {
    
    @NotNull(message = "Patient record ID is required")
    private Long patientRecordId;
    
    @NotBlank(message = "Procedure name is required")
    private String procedureName;
    
    @PastOrPresent(message = "Procedure date must be in the past or present")
    private LocalDate procedureDate;
    
    private String outcome;
} 