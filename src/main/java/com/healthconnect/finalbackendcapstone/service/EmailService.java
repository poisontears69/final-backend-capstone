package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.model.User;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);
    
    void sendHtmlMessage(String to, String subject, String htmlContent);
    
    void sendOtpEmail(User user, String otp, String otpType);
    
    void sendPasswordResetEmail(User user, String resetLink);
    
    void sendWelcomeEmail(User user);
    
    void sendAppointmentConfirmation(User user, String appointmentDetails);
} 