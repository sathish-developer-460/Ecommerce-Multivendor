package com.sathish.ecommerce.multivendor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sathish.ecommerce.multivendor.domain.USER_ROLE;
import com.sathish.ecommerce.multivendor.modal.VerificationCode;
import com.sathish.ecommerce.multivendor.response.ApiResponse;
import com.sathish.ecommerce.multivendor.response.AuthResponse;
import com.sathish.ecommerce.multivendor.response.LoginRequest;
import com.sathish.ecommerce.multivendor.response.SignupRequest;
import com.sathish.ecommerce.multivendor.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest request) throws Exception {
        String jwt = authService.createUser(request);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setMessage("register success");
        response.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody VerificationCode request) throws Exception {
        authService.sentLogingOtp(request.getEmail());

        ApiResponse response = new ApiResponse();
        response.setMessage("otp sent successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest request) throws Exception {

        AuthResponse authResponse = authService.signing(request);

        return ResponseEntity.ok(authResponse);
    }
}