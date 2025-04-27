package com.healthconnect.finalbackendcapstone.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class DoctorProfileResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String fullName;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    
    private String gender;
    private String title;
    private String description;
    private String email;
    private String phoneNumber;
    private String prcNumber;
    private String s2Number;
    private String ptrNumber;
    private String phicNumber;
    private Integer practicingSince;
    private Integer yearsOfExperience;
    private String profilePictureUrl;
    private boolean isVerified;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
} 