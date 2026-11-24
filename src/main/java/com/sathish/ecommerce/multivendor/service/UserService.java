package com.sathish.ecommerce.multivendor.service;

import com.sathish.ecommerce.multivendor.modal.User;

public interface UserService {
    User findUserByJwtToken(String jwt) throws Exception;

    User findUserByEmail(String email);

}