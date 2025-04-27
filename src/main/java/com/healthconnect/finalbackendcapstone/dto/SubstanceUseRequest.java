package com.healthconnect.finalbackendcapstone.dto;

import com.healthconnect.finalbackendcapstone.model.SubstanceUse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SubstanceUseRequest {
    
    @NotNull(message = "Patient record ID is required")
    private Long patientRecordId;
    
    @NotBlank(message = "Substance name is required")
    private String substanceName;
    
    private SubstanceUse.UsageFrequency usageFrequency;
    
    @PastOrPresent(message = "Start date must be in the past or present")
    private LocalDate startDate;
    
    private LocalDate endDate;
} 