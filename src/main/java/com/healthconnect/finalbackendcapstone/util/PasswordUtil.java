package com.healthconnect.finalbackendcapstone.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Utility class for password hashing and verification
 */
@Component
public class PasswordUtil {
    
    private final BCryptPasswordEncoder passwordEncoder;
    
    public PasswordUtil() {
        this.passwordEncoder = new BCryptPasswordEncoder(12); // Using strength 12
    }
    
    /**
     * Encode a raw password
     * @param rawPassword the raw password to encode
     * @return the encoded password
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    /**
     * Verify a raw password against an encoded password
     * @param rawPassword the raw password to check
     * @param encodedPassword the encoded password to check against
     * @return true if the passwords match, false otherwise
     */
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
} 