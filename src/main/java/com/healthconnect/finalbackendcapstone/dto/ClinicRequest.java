package com.healthconnect.finalbackendcapstone.dto;

import com.healthconnect.finalbackendcapstone.model.Clinic.ConsultationMode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClinicRequest {
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    
    @NotNull(message = "Consultation mode is required")
    private ConsultationMode consultationMode = ConsultationMode.IN_CLINIC;
    
    @NotBlank(message = "Clinic name is required")
    private String clinicName;
    
    @NotBlank(message = "Address line 1 is required")
    private String addressLine1;
    
    private String addressLine2;
    
    private String city;
    
    private String province;
    
    private String region;
    
    private String zipCode;
    
    private String landmark;
    
    private String description;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String landlineNumber;
    
    private String phoneNumber;
    
    private String contactPersonName;
    
    @Email(message = "Invalid contact person email format")
    private String contactPersonEmail;
    
    private String contactPersonPhone;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Consultation fee must be non-negative")
    private BigDecimal consultationFee;
} 