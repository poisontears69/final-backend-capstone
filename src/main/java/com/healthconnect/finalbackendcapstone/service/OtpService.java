package com.healthconnect.finalbackendcapstone.service;

public interface OtpService {
    String generateOTP();
    void sendOTP(String mobileNumber, String otp);
    boolean verifyOTP(String mobileNumber, String otp);
}
