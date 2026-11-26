package com.sathish.ecommerce.multivendor.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sathish.ecommerce.multivendor.modal.VerificationCode;
import com.sathish.ecommerce.multivendor.repository.VerificationCodeRepository;
import com.sathish.ecommerce.multivendor.service.VerificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final VerificationCodeRepository verificationCodeRepository;

    @Override
    @Transactional
    public VerificationCode createVerificationCode(String otp, String email) {
        // remove any old OTP for this email so only the latest one is valid
        VerificationCode existing = verificationCodeRepository.findByEmail(email);
        if (existing != null) {
            verificationCodeRepository.delete(existing);
        }

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        return verificationCodeRepository.save(verificationCode);
    }
}