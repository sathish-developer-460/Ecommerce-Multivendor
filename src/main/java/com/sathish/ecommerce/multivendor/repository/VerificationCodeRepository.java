package com.sathish.ecommerce.multivendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sathish.ecommerce.multivendor.modal.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByEmail(String email);

    VerificationCode findByOtp(String otp);
}
