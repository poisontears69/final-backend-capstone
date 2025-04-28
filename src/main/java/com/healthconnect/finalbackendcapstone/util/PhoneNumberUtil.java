package com.healthconnect.finalbackendcapstone.util;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class PhoneNumberUtil {
    
    private static final Pattern PH_MOBILE_PATTERN = Pattern.compile("^(09|\\+639)\\d{9}$");
    
    /**
     * Validates if the given phone number is a valid Philippine mobile number
     * @param phoneNumber the phone number to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidPhilippineNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return false;
        }
        return PH_MOBILE_PATTERN.matcher(phoneNumber.trim()).matches();
    }
    
    /**
     * Formats the given phone number to Philippine format (+63)
     * @param phoneNumber the phone number to format
     * @return formatted phone number or null if invalid
     */
    public String formatToPhilippineNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return null;
        }
        
        // Remove all non-digit characters
        String digits = phoneNumber.replaceAll("\\D", "");
        
        // Handle different cases
        if (digits.length() == 10 && digits.startsWith("9")) {
            // Convert 9XXXXXXXXX to +639XXXXXXXXX
            return "+63" + digits;
        } else if (digits.length() == 11 && digits.startsWith("09")) {
            // Convert 09XXXXXXXXX to +639XXXXXXXXX
            return "+63" + digits.substring(1);
        } else if (digits.length() == 12 && digits.startsWith("639")) {
            // Already in correct format, just add +
            return "+" + digits;
        } else if (digits.length() == 13 && digits.startsWith("6309")) {
            // Convert +6309XXXXXXXX to +639XXXXXXXX
            return "+63" + digits.substring(3);
        }
        
        return null;
    }
    
    /**
     * Formats the given phone number to local Philippine format (09)
     * @param phoneNumber the phone number to format
     * @return formatted phone number or null if invalid
     */
    public String formatToLocalPhilippineNumber(String phoneNumber) {
        String formatted = formatToPhilippineNumber(phoneNumber);
        if (formatted != null && formatted.startsWith("+63")) {
            return "0" + formatted.substring(3);
        }
        return null;
    }
} 