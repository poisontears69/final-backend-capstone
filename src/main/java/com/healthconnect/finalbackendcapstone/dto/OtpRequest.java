package com.healthconnect.finalbackendcapstone.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    // Optional phone number for SMS OTPs in the future
    private String phoneNumber;
    
    // Type of OTP (e.g., "EMAIL_VERIFICATION", "PASSWORD_RESET")
    private String otpType;
}