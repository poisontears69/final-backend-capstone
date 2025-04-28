package com.healthconnect.finalbackendcapstone.service.impl;

import com.healthconnect.finalbackendcapstone.model.OtpToken;
import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.repository.OtpTokenRepository;
import com.healthconnect.finalbackendcapstone.service.EmailService;
import com.healthconnect.finalbackendcapstone.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {

    private final OtpTokenRepository otpTokenRepository;
    private final EmailService emailService;
    
    private static final String OTP_CHARS = "0123456789";
    private final Random random = new SecureRandom();
    
    @Value("${otp.expiration:300000}") // Default: 5 minutes in milliseconds
    private long otpExpirationMs;
    
    @Value("${otp.length:6}") // Default: 6 digits
    private int otpLength;

    @Override
    @Transactional
    public String generateAndSendOtp(User user, OtpToken.OtpType otpType) {
        // Invalidate any existing OTPs for this user and type
        invalidateOtps(user, otpType);
        
        // Generate a new OTP
        String otp = generateOtp();
        
        // Create and save the OTP token
        OtpToken otpToken = new OtpToken();
        otpToken.setToken(otp);
        otpToken.setUser(user);
        otpToken.setOtpType(otpType);
        otpToken.setExpiresAt(LocalDateTime.now().plusSeconds(otpExpirationMs / 1000)); // Convert ms to seconds
        otpToken.setUsed(false);
        
        otpTokenRepository.save(otpToken);
        
        // Send OTP via email
        emailService.sendOtpEmail(user, otp, otpType.toString());
        
        log.info("Generated and sent OTP for user {} with type {}", user.getEmail(), otpType);
        return otp;
    }

    @Override
    @Transactional
    public boolean verifyOtp(User user, String otp, OtpToken.OtpType otpType) {
        Optional<OtpToken> otpTokenOpt = otpTokenRepository.findByUserAndOtpTypeAndIsUsedFalse(user, otpType);
        
        if (otpTokenOpt.isEmpty()) {
            log.warn("No valid OTP found for user {} with type {}", user.getEmail(), otpType);
            return false;
        }
        
        OtpToken otpToken = otpTokenOpt.get();
        
        // Check if OTP is expired
        if (otpToken.isExpired()) {
            log.warn("OTP expired for user {} with type {}", user.getEmail(), otpType);
            return false;
        }
        
        // Check if OTP matches
        if (!otpToken.getToken().equals(otp)) {
            log.warn("Invalid OTP provided for user {} with type {}", user.getEmail(), otpType);
            return false;
        }
        
        // Mark OTP as used
        otpToken.setUsed(true);
        otpToken.setVerifiedAt(LocalDateTime.now());
        otpTokenRepository.save(otpToken);
        
        log.info("OTP verified successfully for user {} with type {}", user.getEmail(), otpType);
        return true;
    }

    @Override
    @Transactional
    public void invalidateOtps(User user, OtpToken.OtpType otpType) {
        otpTokenRepository.findByUserAndOtpType(user, otpType).forEach(token -> {
            token.setUsed(true);
            otpTokenRepository.save(token);
        });
        log.info("Invalidated all OTPs for user {} with type {}", user.getEmail(), otpType);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 * * * *") // Run once every hour
    public void cleanupExpiredOtps() {
        otpTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
        log.info("Cleaned up expired OTPs");
    }

    @Override
    @Transactional
    public String resendOtp(User user, OtpToken.OtpType otpType) {
        // Check if there's a valid OTP that was recently generated
        Optional<OtpToken> existingOtp = otpTokenRepository.findValidTokenByUserAndType(
                user, otpType, LocalDateTime.now());
        
        if (existingOtp.isPresent()) {
            // Resend the existing OTP
            OtpToken otpToken = existingOtp.get();
            emailService.sendOtpEmail(user, otpToken.getToken(), otpType.toString());
            log.info("Resent existing OTP for user {} with type {}", user.getEmail(), otpType);
            return otpToken.getToken();
        } else {
            // Generate a new OTP
            return generateAndSendOtp(user, otpType);
        }
    }
    
    /**
     * Generates a random OTP of the specified length
     */
    private String generateOtp() {
        StringBuilder otp = new StringBuilder(otpLength);
        for (int i = 0; i < otpLength; i++) {
            otp.append(OTP_CHARS.charAt(random.nextInt(OTP_CHARS.length())));
        }
        return otp.toString();
    }
} 