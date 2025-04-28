package com.healthconnect.finalbackendcapstone.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorProfileRequest {
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    private String middleName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    private String suffix;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    
    private String gender; // Will be converted to enum
    
    private String title; // Medical specialization/title (e.g., "Cardiologist")
    
    private String description;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @Pattern(regexp = "^(09|\\+639)\\d{9}$", 
            message = "Phone number must be a valid Philippine mobile number (e.g., 09123456789 or +639123456789)")
    private String phoneNumber;
    
    @NotBlank(message = "PRC number is required")
    private String prcNumber;
    
    private String s2Number;
    
    private String ptrNumber;
    
    private String phicNumber;
    
    private Integer practicingSince;
    
    private String profilePictureUrl;
    
    // Utility method to convert gender string to enum
    public DoctorProfile.Gender getGenderEnum() {
        return DoctorProfile.Gender.fromString(this.gender);
    }
} 