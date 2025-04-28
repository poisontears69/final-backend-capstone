package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.model.OtpToken;
import com.healthconnect.finalbackendcapstone.model.User;

public interface OtpService {

    /**
     * Generates a new OTP for a user and sends it via email
     * @param user the user to generate OTP for
     * @param otpType the type of OTP (e.g., EMAIL_VERIFICATION, PASSWORD_RESET)
     * @return the generated OTP token
     */
    String generateAndSendOtp(User user, OtpToken.OtpType otpType);
    
    /**
     * Verifies an OTP for a given user and type
     * @param user the user
     * @param otp the OTP to verify
     * @param otpType the type of OTP
     * @return true if the OTP is valid, false otherwise
     */
    boolean verifyOtp(User user, String otp, OtpToken.OtpType otpType);
    
    /**
     * Invalidates all OTPs for a given user and type
     * @param user the user
     * @param otpType the type of OTP
     */
    void invalidateOtps(User user, OtpToken.OtpType otpType);
    
    /**
     * Cleans up expired OTPs from the database
     */
    void cleanupExpiredOtps();
    
    /**
     * Resends an OTP to a user
     * @param user the user
     * @param otpType the type of OTP
     * @return the OTP token (new or existing if still valid)
     */
    String resendOtp(User user, OtpToken.OtpType otpType);
} 