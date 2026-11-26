package com.sathish.ecommerce.multivendor.utils;

import java.security.SecureRandom;

// Logic for generating a new OTP
public class OtpUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private OtpUtil() {
    }

    public static String generateOtp() {
        int otpLength = 6;
        StringBuilder otp = new StringBuilder(otpLength);

        for (int i = 0; i < otpLength; i++) {
            otp.append(RANDOM.nextInt(10));
        }
        return otp.toString();
    }
}