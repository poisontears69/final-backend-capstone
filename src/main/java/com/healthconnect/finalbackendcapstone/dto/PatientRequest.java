package com.healthconnect.finalbackendcapstone.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest {
    
    @Size(min = 1, max = 255, message = "First name must be between 1 and 255 characters")
    private String firstName;
    
    @Size(max = 100, message = "Middle name must be at most 100 characters")
    private String middleName;
    
    @Size(min = 1, max = 255, message = "Last name must be between 1 and 255 characters")
    private String lastName;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    
    private String gender;
    
    @Size(max = 255, message = "Address line 1 must be at most 255 characters")
    private String addressLine1;
    
    @Size(max = 255, message = "Address line 2 must be at most 255 characters")
    private String addressLine2;
    
    @Size(max = 255, message = "City must be at most 255 characters")
    private String city;
    
    @Size(max = 100, message = "Province must be at most 100 characters")
    private String province;
    
    @Size(max = 20, message = "Zip code must be at most 20 characters")
    @Pattern(regexp = "^[0-9A-Za-z\\-\\s]*$", message = "Zip code contains invalid characters")
    private String zipCode;
    
    private String profilePictureUrl;
} 