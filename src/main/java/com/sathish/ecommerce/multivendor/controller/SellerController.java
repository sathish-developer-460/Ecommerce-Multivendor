package com.sathish.ecommerce.multivendor.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sathish.ecommerce.multivendor.domain.AccountStatus;
import com.sathish.ecommerce.multivendor.modal.Seller;
import com.sathish.ecommerce.multivendor.modal.VerificationCode;
import com.sathish.ecommerce.multivendor.request.LoginRequest;
import com.sathish.ecommerce.multivendor.response.ApiResponse;
import com.sathish.ecommerce.multivendor.response.AuthResponse;
import com.sathish.ecommerce.multivendor.repository.VerificationCodeRepository;
import com.sathish.ecommerce.multivendor.service.AuthService;
import com.sathish.ecommerce.multivendor.service.EmailService;
import com.sathish.ecommerce.multivendor.service.SellerService;

import com.sathish.ecommerce.multivendor.service.VerificationService;
import com.sathish.ecommerce.multivendor.service.impl.CustomUserServiceImpl;
import com.sathish.ecommerce.multivendor.utils.OtpUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    private final AuthService authService;
    private final SellerService sellerService;
    private final VerificationService verificationService;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;

    // Step 1: seller asks for a login OTP
    @PostMapping("/sent/login-otp")
    public ResponseEntity<ApiResponse> sentLoginOtp(@RequestBody LoginRequest req) throws Exception {
        // throws if no seller exists with this email
        sellerService.getSellerByEmail(req.getEmail());

        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = verificationService.createVerificationCode(otp, req.getEmail());

        emailService.sendVerificationOtpEmail(req.getEmail(), verificationCode.getOtp(),
                "Zosh Bazaar Login OTP", "Your login OTP is - ");

        ApiResponse res = new ApiResponse();
        res.setMessage("OTP sent successfully");
        return ResponseEntity.ok(res);
    }

    // Step 2: seller submits the OTP and gets a JWT
    @PostMapping("/verify/login-otp")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {
        req.setEmail(CustomUserServiceImpl.SELLER_PREFIX + req.getEmail());
        AuthResponse authResponse = authService.signing(req);
        return ResponseEntity.ok(authResponse);
    }

    // Verify seller email after registration:
    // PATCH /sellers/verify/123456?email=a@b.com
    // PATCH => you have to give ch.sathish369@gmail.com mail id in the Query Params
    // in postman (Test the URL) to become "emailVerified" : true
    // http://localhost:8080/sellers/verify/920481?email=ch.sathish369@gmail.com
    // @PatchMapping("/verify/{otp}")
    // public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp,
    // @RequestParam String email) throws Exception {
    // Seller seller = sellerService.verifyEmail(email, otp);
    // return new ResponseEntity<>(seller, HttpStatus.OK);
    // }

    // with no @RequestParam:
    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp)
            throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
        if (verificationCode == null) {
            throw new IllegalArgumentException("wrong otp...");
        }
        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    // Register a new seller and email a verification link
    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception {
        Seller savedSeller = sellerService.createSeller(seller);

        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = verificationService.createVerificationCode(otp, savedSeller.getEmail());

        String subject = "Zosh Bazaar Email Verification Code";
        String link = "http://localhost:3000/verify-seller/" + otp
                + "?email=" + URLEncoder.encode(savedSeller.getEmail(), StandardCharsets.UTF_8);
        String text = "Welcome to Zosh Bazaar, verify your account using this link: " + link;

        emailService.sendVerificationOtpEmail(savedSeller.getEmail(), verificationCode.getOtp(), subject, text);

        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception {
        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false) AccountStatus status) {
        List<Seller> sellers = sellerService.getAllSellers(status);
        return ResponseEntity.ok(sellers);
    }

    @PatchMapping
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt,
            @RequestBody Seller seller) throws Exception {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}