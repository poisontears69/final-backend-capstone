package com.healthconnect.finalbackendcapstone.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private Long id;
    private String email;
    private String phoneNumber;
    private String role;
    private String token;
    private boolean isEmailVerified;
    private boolean isPhoneVerified;
    private boolean isActive;
} 