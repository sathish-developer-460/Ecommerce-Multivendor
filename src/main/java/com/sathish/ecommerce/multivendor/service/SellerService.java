package com.sathish.ecommerce.multivendor.service;

import com.sathish.ecommerce.multivendor.modal.Seller;
import com.sathish.ecommerce.multivendor.domain.AccountStatus;
import java.util.List;

public interface SellerService {
    Seller getSellerProfile(String jwt);

    Seller createSeller(Seller seller);

    Seller getSellerById(Long id);

    Seller getSellerByEmail(String email);

    List<Seller> getAllSellers(AccountStatus status);

    Seller updatSeller(Long id, Seller seller);

    void deleteSeller(Long id);

    Seller verifyEmail(String email, String otp);

    Seller updateSellerAccountStatus(Long sellerId, AccountStatus status);
}
