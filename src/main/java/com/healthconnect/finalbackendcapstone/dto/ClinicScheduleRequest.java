package com.healthconnect.finalbackendcapstone.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
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
    
    @Min(value = 15, message = "Consultation duration must be at least 15 minutes")
    private Integer consultationDurationMinutes = 30;
    
    @Min(value = 1, message = "Maximum parallel appointments must be at least 1")
    private Integer maxParallelAppointments = 1;
    
    private Boolean isActive = true;
} 