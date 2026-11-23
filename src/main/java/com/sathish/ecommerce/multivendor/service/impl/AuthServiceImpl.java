package com.sathish.ecommerce.multivendor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sathish.ecommerce.multivendor.config.JwtProvider;
import com.sathish.ecommerce.multivendor.domain.USER_ROLE;
import com.sathish.ecommerce.multivendor.modal.Cart;
import com.sathish.ecommerce.multivendor.modal.User;
import com.sathish.ecommerce.multivendor.modal.VerificationCode;
import com.sathish.ecommerce.multivendor.repository.CartRepository;
import com.sathish.ecommerce.multivendor.repository.UserRepository;
import com.sathish.ecommerce.multivendor.repository.VerificationCodeRepository;
import com.sathish.ecommerce.multivendor.response.SignupRequest;
import com.sathish.ecommerce.multivendor.service.AuthService;
import com.sathish.ecommerce.multivendor.service.EmailService;
import com.sathish.ecommerce.multivendor.utils.OtpUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;

    @Override
    public void sentLogingOtp(String email) {
        String SIGNING_PREFIX = "signin_";
        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new IllegalArgumentException("user not exist with provided email");
            }
        }
        VerificationCode isExist = verificationCodeRepository.findByEmail(email);
        if (isExist != null) {
            verificationCodeRepository.delete(isExist);
        }
        // Generating new OTP
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject = "zosh bazaar login/signup otp";
        String text = "your login/signup otp is - " + otp;

        try {
            emailService.sendVerificationOtpEmail(email, otp, subject, text);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to send verification OTP email", e);
        }
    }

    @Override
    public String createUser(SignupRequest request) {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());

        if (verificationCode == null || !verificationCode.getOtp().equals(request.getOtp())) {
            throw new BadCredentialsException("wrong otp...");
        }

        User user = userRepository.findByEmail(request.getEmail());
        // whenever a new user is created which new user has to get one OTP to his email
        // new user need provide correct OTP, without providing right(correct) OTP user
        // won't able to create new account.

        if (user == null) {
            User createdUser = new User();
            createdUser.setEmail(request.getEmail());
            createdUser.setFullName(request.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("1234567890"); // dummy mobile number
            createdUser.setPassword(passwordEncoder.encode(request.getOtp()));

            user = userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }
}