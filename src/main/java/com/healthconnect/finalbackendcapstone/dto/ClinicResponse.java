package com.healthconnect.finalbackendcapstone.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ClinicResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private String clinicName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String province;
    private String region;
    private String zipCode;
    private String landmark;
    private String description;
    private String email;
    private String landlineNumber;
    private String phoneNumber;
    private String contactPersonName;
    private String contactPersonEmail;
    private String contactPersonPhone;
    private BigDecimal consultationFee;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Additional doctor details
    private String doctorSpecialty;
    private String doctorEmail;
    private String doctorPhoneNumber;
} 