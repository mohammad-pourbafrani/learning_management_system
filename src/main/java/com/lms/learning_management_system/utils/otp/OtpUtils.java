package com.lms.learning_management_system.utils.otp;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OtpUtils {
    private final SecureRandom secureRandom = new SecureRandom();

    public String generateOtp() {
        int OTP_LENGTH = 6;
        return String.format("%0" + OTP_LENGTH + "d", secureRandom.nextInt(1_000_000));
    }

}
