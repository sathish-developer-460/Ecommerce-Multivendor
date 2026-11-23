package com.sathish.ecommerce.multivendor.service;

import com.sathish.ecommerce.multivendor.modal.User;

public interface UserService {
    User findUserByJwtToken(String jwt);

    User findUserByEmail(String email);

}