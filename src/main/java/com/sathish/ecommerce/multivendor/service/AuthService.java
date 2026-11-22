package com.sathish.ecommerce.multivendor.service;

import com.sathish.ecommerce.multivendor.response.SignupRequest;

public interface AuthService {

    String createUser(SignupRequest request);

}
