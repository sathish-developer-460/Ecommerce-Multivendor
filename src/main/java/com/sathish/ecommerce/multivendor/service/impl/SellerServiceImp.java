package com.sathish.ecommerce.multivendor.service.impl;

import com.sathish.ecommerce.multivendor.config.JwtProvider;
import com.sathish.ecommerce.multivendor.domain.AccountStatus;
import com.sathish.ecommerce.multivendor.modal.Seller;
import com.sathish.ecommerce.multivendor.repository.SellerRepository;
import com.sathish.ecommerce.multivendor.service.SellerService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerServiceImp implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;

    @Override
    public Seller getSellerProfile(String jwt) {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) {
        return null;
    }

    @Override
    public Seller getSellerById(Long id) {
        return null;
    }

    @Override
    public Seller getSellerByEmail(String email) {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null) {
            throw new Exception("seller not found...");
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        return List.of();
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) {

    }
}
