package com.sathish.ecommerce.multivendor.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sathish.ecommerce.multivendor.config.JwtProvider;
import com.sathish.ecommerce.multivendor.domain.AccountStatus;
import com.sathish.ecommerce.multivendor.domain.USER_ROLE;
import com.sathish.ecommerce.multivendor.modal.Address;
import com.sathish.ecommerce.multivendor.modal.Seller;
import com.sathish.ecommerce.multivendor.modal.VerificationCode;
import com.sathish.ecommerce.multivendor.repository.AddressRepository;
import com.sathish.ecommerce.multivendor.repository.SellerRepository;
import com.sathish.ecommerce.multivendor.repository.VerificationCodeRepository;
import com.sathish.ecommerce.multivendor.service.SellerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final VerificationCodeRepository verificationCodeRepository; // adjust to your repo name

    @Override
    public Seller getSellerProfile(String jwt) {
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return getSellerByEmail(email);
    }

    @Override
    @Transactional
    public Seller createSeller(Seller seller) {
        if (sellerRepository.findByEmail(seller.getEmail()) != null) {
            throw new IllegalStateException("Seller already exists, use a different email");
        }
        if (seller.getPickupAddress() == null) {
            throw new IllegalArgumentException("Pickup address is required");
        }

        Address savedAddress = addressRepository.save(seller.getPickupAddress());

        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(savedAddress);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());
        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Seller not found with id " + id));
    }

    @Override
    public Seller getSellerByEmail(String email) {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null) {
            throw new IllegalStateException("Seller not found with email " + email);
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        return status == null ? sellerRepository.findAll() : sellerRepository.findByAccountStatus(status);
    }

    @Override
    @Transactional
    public Seller updateSeller(Long id, Seller seller) {
        Seller existing = getSellerById(id);

        if (seller.getSellerName() != null) {
            existing.setSellerName(seller.getSellerName());
        }
        if (seller.getMobile() != null) {
            existing.setMobile(seller.getMobile());
        }
        if (seller.getEmail() != null && !seller.getEmail().equals(existing.getEmail())) {
            Seller other = sellerRepository.findByEmail(seller.getEmail());
            if (other != null && !other.getId().equals(existing.getId())) {
                throw new IllegalStateException("Email is already in use");
            }
            existing.setEmail(seller.getEmail());
        }
        if (seller.getGSTIN() != null) {
            existing.setGSTIN(seller.getGSTIN());
        }

        // Business details
        if (seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null) {
            if (existing.getBusinessDetails() == null) {
                existing.setBusinessDetails(seller.getBusinessDetails());
            } else {
                existing.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
            }
        }

        // Bank details
        if (seller.getBankDetails() != null
                && seller.getBankDetails().getAccountHolderName() != null
                && seller.getBankDetails().getIfscCode() != null
                && seller.getBankDetails().getAccountNumber() != null) {
            if (existing.getBankDetails() == null) {
                existing.setBankDetails(seller.getBankDetails());
            } else {
                existing.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
                existing.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
                existing.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
            }
        }

        // Pickup address
        Address newAddr = seller.getPickupAddress();
        if (newAddr != null && newAddr.getAddress() != null && newAddr.getMobile() != null
                && newAddr.getCity() != null && newAddr.getState() != null) {
            Address addr = existing.getPickupAddress();
            if (addr == null) {
                existing.setPickupAddress(addressRepository.save(newAddr));
            } else {
                addr.setAddress(newAddr.getAddress());
                addr.setCity(newAddr.getCity());
                addr.setState(newAddr.getState());
                addr.setMobile(newAddr.getMobile());
                if (newAddr.getPincode() != null) {
                    addr.setPincode(newAddr.getPincode());
                }
            }
        }

        return sellerRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteSeller(Long id) {
        sellerRepository.delete(getSellerById(id));
    }

    @Override
    @Transactional
    public Seller verifyEmail(String email, String otp) {
        VerificationCode code = verificationCodeRepository.findByEmail(email);
        if (code == null || otp == null || !otp.equals(code.getOtp())) {
            throw new IllegalArgumentException("Invalid OTP");
        }
        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }

    @Override
    @Transactional
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) {
        Seller seller = getSellerById(sellerId);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }
}