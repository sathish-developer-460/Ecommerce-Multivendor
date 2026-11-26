package com.sathish.ecommerce.multivendor.service;

import com.sathish.ecommerce.multivendor.domain.USER_ROLE;
import com.sathish.ecommerce.multivendor.response.AuthResponse;
import com.sathish.ecommerce.multivendor.request.LoginRequest;
import com.sathish.ecommerce.multivendor.response.SignupRequest;

public interface AuthService {

    void sentLogingOtp(String email, USER_ROLE role) throws Exception;

    String createUser(SignupRequest request) throws Exception;

    AuthResponse signing(LoginRequest request);
}
