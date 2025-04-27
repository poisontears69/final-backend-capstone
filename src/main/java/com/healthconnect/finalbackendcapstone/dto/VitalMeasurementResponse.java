package com.healthconnect.finalbackendcapstone.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VitalMeasurementResponse {
    private Long id;
    private Long patientRecordId;
    private Integer vitalTypeId;
    private String vitalTypeName;
    private String value;
    private String unit;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    // Additional details for context
    private String patientName;
    private String doctorName;
} 