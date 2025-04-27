package com.healthconnect.finalbackendcapstone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DoctorSocialLinkRequest {
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    
    @NotBlank(message = "URL is required")
    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?$", 
            message = "Invalid URL format")
    private String url;
} 