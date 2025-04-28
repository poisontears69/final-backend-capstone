package com.healthconnect.finalbackendcapstone.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OtpVerificationRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "OTP code is required")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits")
    private String otp;
    
    // Type of OTP being verified (e.g., "EMAIL_VERIFICATION", "PASSWORD_RESET")
    private String otpType;
} 