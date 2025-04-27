package com.healthconnect.finalbackendcapstone.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.healthconnect.finalbackendcapstone.model.Clinic;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
public class ClinicScheduleResponse {
    private Long id;
    private Long clinicId;
    private String clinicName;
    
    private DayOfWeek dayOfWeek;
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;
    
    private Integer consultationDurationMinutes;
    private Integer maxParallelAppointments;
    private Boolean isActive;
    
    // Additional clinic details
    private String clinicAddress;
    private String clinicCity;
    private String clinicProvince;
    private Clinic.ConsultationMode consultationMode;
} 