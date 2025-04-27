package com.healthconnect.finalbackendcapstone.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class MedicalProcedureResponse {
    private Long id;
    private Long patientRecordId;
    private String patientName;
    private String procedureName;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate procedureDate;
    
    private String outcome;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    // Additional context
    private String doctorName;
    private Integer patientAge;
    private String patientGender;
} 