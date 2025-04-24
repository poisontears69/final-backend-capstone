package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.dto.*;
import com.healthconnect.finalbackendcapstone.service.OtpService;
import com.healthconnect.finalbackendcapstone.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final OtpService otpService;
    private final UserService userService;

    public AuthController(OtpService otpService, UserService userService) {
        this.otpService = otpService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        try {
            // 1. Save user (unverified)
            userService.createUser(request);

            // 2. Generate and send OTP
            String otp = otpService.generateOTP();
            otpService.sendOTP(request.getMobileNumber(), otp);

            return ResponseEntity.ok(new ApiResponse(true, "OTP sent to your mobile number"));
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt flag
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(false, "Failed to process registration: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse> verify(@RequestBody VerifyRequest request) {
        try {
            boolean verified = otpService.verifyOTP(
                    request.getMobileNumber(),
                    request.getOtp()
            );

            if (verified) {
                userService.markUserAsVerified(request.getMobileNumber());
                return ResponseEntity.ok(new ApiResponse(true, "Mobile number verified"));
            }

            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Invalid OTP"));
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(false, "Verification failed: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}