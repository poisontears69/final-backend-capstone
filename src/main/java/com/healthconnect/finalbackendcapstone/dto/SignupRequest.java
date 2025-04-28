package com.healthconnect.finalbackendcapstone.dto;

import com.healthconnect.finalbackendcapstone.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(09|\\+639)\\d{9}$", 
            message = "Phone number must be a valid Philippine mobile number (e.g., 09123456789 or +639123456789)")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private String role = "PATIENT"; // Default role
} 