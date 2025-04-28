package com.healthconnect.finalbackendcapstone.service.impl;

import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    
    // Optional: If you want to use Thymeleaf for HTML templates
    // private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.name:HealthConnect}")
    private String appName;

    @Override
    @Async
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
            log.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    @Async
    public void sendHtmlMessage(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            emailSender.send(message);
            log.info("HTML email sent successfully to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    @Override
    @Async
    public void sendOtpEmail(User user, String otp, String otpType) {
        String subject = appName + " - Your OTP for " + otpType;
        String greeting = "Hello " + user.getFullName() + ",";
        
        StringBuilder content = new StringBuilder();
        content.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>");
        content.append("<h2 style='color: #4a86e8;'>").append(appName).append("</h2>");
        content.append("<p>").append(greeting).append("</p>");
        content.append("<p>Your one-time password (OTP) for ").append(otpType).append(" is:</p>");
        content.append("<h1 style='font-size: 24px; letter-spacing: 5px; background-color: #f2f2f2; padding: 10px; text-align: center;'>")
               .append(otp).append("</h1>");
        content.append("<p>This OTP is valid for 5 minutes. Please do not share this with anyone.</p>");
        content.append("<p>If you did not request this OTP, please ignore this email or contact support.</p>");
        content.append("<p>Best regards,<br/>The ").append(appName).append(" Team</p>");
        content.append("</div>");
        
        sendHtmlMessage(user.getEmail(), subject, content.toString());
    }

    @Override
    @Async
    public void sendPasswordResetEmail(User user, String resetLink) {
        String subject = appName + " - Password Reset Request";
        String greeting = "Hello " + user.getFullName() + ",";
        
        StringBuilder content = new StringBuilder();
        content.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>");
        content.append("<h2 style='color: #4a86e8;'>").append(appName).append("</h2>");
        content.append("<p>").append(greeting).append("</p>");
        content.append("<p>We received a request to reset your password. Click the button below to reset it:</p>");
        content.append("<div style='text-align: center; margin: 20px 0;'>");
        content.append("<a href='").append(resetLink).append("' style='background-color: #4a86e8; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Reset Password</a>");
        content.append("</div>");
        content.append("<p>If you did not request a password reset, please ignore this email or contact support.</p>");
        content.append("<p>This link will expire in 30 minutes.</p>");
        content.append("<p>Best regards,<br/>The ").append(appName).append(" Team</p>");
        content.append("</div>");
        
        sendHtmlMessage(user.getEmail(), subject, content.toString());
    }

    @Override
    @Async
    public void sendWelcomeEmail(User user) {
        String subject = "Welcome to " + appName + "!";
        String greeting = "Hello " + user.getFullName() + ",";
        
        StringBuilder content = new StringBuilder();
        content.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>");
        content.append("<h2 style='color: #4a86e8;'>Welcome to ").append(appName).append("!</h2>");
        content.append("<p>").append(greeting).append("</p>");
        content.append("<p>Thank you for joining ").append(appName).append(". We're excited to have you as part of our community!</p>");
        content.append("<p>With your new account, you can:</p>");
        content.append("<ul>");
        content.append("<li>Book appointments with healthcare professionals</li>");
        content.append("<li>Access your health records securely</li>");
        content.append("<li>Receive timely health updates and reminders</li>");
        content.append("</ul>");
        content.append("<p>If you have any questions or need assistance, please don't hesitate to contact our support team.</p>");
        content.append("<p>Best regards,<br/>The ").append(appName).append(" Team</p>");
        content.append("</div>");
        
        sendHtmlMessage(user.getEmail(), subject, content.toString());
    }

    @Override
    @Async
    public void sendAppointmentConfirmation(User user, String appointmentDetails) {
        String subject = appName + " - Appointment Confirmation";
        String greeting = "Hello " + user.getFullName() + ",";
        
        StringBuilder content = new StringBuilder();
        content.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>");
        content.append("<h2 style='color: #4a86e8;'>").append(appName).append("</h2>");
        content.append("<p>").append(greeting).append("</p>");
        content.append("<p>Your appointment has been confirmed. Here are the details:</p>");
        content.append("<div style='background-color: #f2f2f2; padding: 15px; border-radius: 5px;'>");
        content.append(appointmentDetails);
        content.append("</div>");
        content.append("<p>If you need to reschedule or cancel, please log in to your account or contact us.</p>");
        content.append("<p>Best regards,<br/>The ").append(appName).append(" Team</p>");
        content.append("</div>");
        
        sendHtmlMessage(user.getEmail(), subject, content.toString());
    }
} 