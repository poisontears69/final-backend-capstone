package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.dto.ApiResponse;
import com.healthconnect.finalbackendcapstone.dto.OtpRequest;
import com.healthconnect.finalbackendcapstone.dto.OtpVerificationRequest;
import com.healthconnect.finalbackendcapstone.model.OtpToken;
import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.repository.UserRepository;
import com.healthconnect.finalbackendcapstone.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/otp")
@RequiredArgsConstructor
@Slf4j
public class OtpController {

    private final OtpService otpService;
    private final UserRepository userRepository;

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<Void>> requestOtp(@Valid @RequestBody OtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + request.getEmail()));
        
        try {
            OtpToken.OtpType otpType = OtpToken.OtpType.valueOf(request.getOtpType());
            otpService.generateAndSendOtp(user, otpType);
            
            return ResponseEntity.ok(ApiResponse.success("OTP sent successfully to your email."));
        } catch (IllegalArgumentException e) {
            log.error("Invalid OTP type: {}", request.getOtpType(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid OTP type."));
        } catch (Exception e) {
            log.error("Failed to send OTP to user {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send OTP. Please try again later."));
        }
    }
    
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(@Valid @RequestBody OtpVerificationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + request.getEmail()));
        
        try {
            OtpToken.OtpType otpType = OtpToken.OtpType.valueOf(request.getOtpType());
            boolean isValid = otpService.verifyOtp(user, request.getOtp(), otpType);
            
            if (isValid) {
                // Update user status based on OTP type
                if (otpType == OtpToken.OtpType.EMAIL_VERIFICATION) {
                    user.setEmailVerified(true);
                    userRepository.save(user);
                } else if (otpType == OtpToken.OtpType.PHONE_VERIFICATION) {
                    user.setPhoneVerified(true);
                    userRepository.save(user);
                }
                
                return ResponseEntity.ok(ApiResponse.success("OTP verified successfully."));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid or expired OTP."));
            }
        } catch (IllegalArgumentException e) {
            log.error("Invalid OTP type: {}", request.getOtpType(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid OTP type."));
        } catch (Exception e) {
            log.error("Failed to verify OTP for user {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to verify OTP. Please try again later."));
        }
    }
    
    @PostMapping("/resend")
    public ResponseEntity<ApiResponse<Void>> resendOtp(@Valid @RequestBody OtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + request.getEmail()));
        
        try {
            OtpToken.OtpType otpType = OtpToken.OtpType.valueOf(request.getOtpType());
            otpService.resendOtp(user, otpType);
            
            return ResponseEntity.ok(ApiResponse.success("OTP resent successfully to your email."));
        } catch (IllegalArgumentException e) {
            log.error("Invalid OTP type: {}", request.getOtpType(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid OTP type."));
        } catch (Exception e) {
            log.error("Failed to resend OTP to user {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to resend OTP. Please try again later."));
        }
    }
} 