package com.sathish.ecommerce.multivendor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sathish.ecommerce.multivendor.domain.AccountStatus;
import com.sathish.ecommerce.multivendor.modal.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);

    List<Seller> findByAccountStatus(AccountStatus status);
}