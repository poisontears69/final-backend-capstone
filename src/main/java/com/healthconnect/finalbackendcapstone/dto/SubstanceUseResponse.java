package com.healthconnect.finalbackendcapstone.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.healthconnect.finalbackendcapstone.model.SubstanceUse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class SubstanceUseResponse {
    private Long id;
    private Long patientRecordId;
    private String patientName;
    private String substanceName;
    private SubstanceUse.UsageFrequency usageFrequency;
    private String usageFrequencyDisplay;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    private boolean isCurrentUse;
    
    // Additional fields for context
    private String doctorName;
} 