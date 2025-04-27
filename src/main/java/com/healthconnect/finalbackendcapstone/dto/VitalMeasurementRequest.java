package com.healthconnect.finalbackendcapstone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VitalMeasurementRequest {
    
    @NotNull(message = "Patient record ID is required")
    private Long patientRecordId;
    
    @NotNull(message = "Vital type ID is required")
    private Integer vitalTypeId;
    
    @NotBlank(message = "Value is required")
    @Size(max = 50, message = "Value must be less than 50 characters")
    private String value;
    
    private LocalDateTime recordedAt = LocalDateTime.now();
} 