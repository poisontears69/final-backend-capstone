package com.healthconnect.finalbackendcapstone.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class ClinicScheduleRequest {
    
    @NotNull(message = "Clinic ID is required")
    private Long clinicId;
    
    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;
    
    @NotNull(message = "Open time is required")
    private LocalTime openTime;
    
    @NotNull(message = "Close time is required")
    private LocalTime closeTime;
    
    private Boolean isActive = true;
} 