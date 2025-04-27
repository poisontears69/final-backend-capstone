package com.healthconnect.finalbackendcapstone.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VitalTypeRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String unit;
    
    private String description;
} 