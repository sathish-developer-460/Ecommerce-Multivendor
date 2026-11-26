package com.sathish.ecommerce.multivendor.service;

import com.sathish.ecommerce.multivendor.modal.VerificationCode;

public interface VerificationService {

    VerificationCode createVerificationCode(String otp, String email);
}