package com.healthconnect.finalbackendcapstone.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupResponse {
    private Long id;
    private String email;
    private String phoneNumber;
    private String role;
    private boolean isEmailVerified;
    private boolean isPhoneVerified;
    private String message;
}
