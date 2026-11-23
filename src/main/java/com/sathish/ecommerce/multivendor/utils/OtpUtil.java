package com.sathish.ecommerce.multivendor.utils;

import java.util.Random;

//Writing the logic for generating a new OTP
public class OtpUtil {
    public static String generateOtp() {
        int otpLength = 6;

        Random random = new Random();
        StringBuilder otp = new StringBuilder(otpLength);

        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
