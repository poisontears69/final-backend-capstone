package com.healthconnect.finalbackendcapstone.service.impl;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.healthconnect.finalbackendcapstone.service.OtpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class TwilioOTPService implements OtpService {
    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    private final Map<String, String> otpStorage = new HashMap<>();
    private final Random random = new Random();

    @Override
    public String generateOTP() {
        return String.format("%06d", random.nextInt(999999));
    }

    @Override
    public void sendOTP(String mobileNumber, String otp) {
        // Store OTP for verification
        otpStorage.put(mobileNumber, otp);

        // Send SMS via Twilio
        Message.creator(
                new PhoneNumber(mobileNumber),
                new PhoneNumber(twilioPhoneNumber),
                "Your verification code is: " + otp
        ).create();
    }

    @Override
    public boolean verifyOTP(String mobileNumber, String otp) {
        String storedOtp = otpStorage.get(mobileNumber);
        return storedOtp != null && storedOtp.equals(otp);
    }
}
