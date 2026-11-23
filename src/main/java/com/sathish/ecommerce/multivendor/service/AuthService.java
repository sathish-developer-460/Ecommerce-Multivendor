package com.sathish.ecommerce.multivendor.service;

import com.sathish.ecommerce.multivendor.response.SignupRequest;

public interface AuthService {

    void sentLogingOtp(String email);

    String createUser(SignupRequest request) throws Exception;

}
