package com.sathish.ecommerce.multivendor.service.impl;

import org.springframework.stereotype.Service;

import com.sathish.ecommerce.multivendor.config.JwtProvider;
import com.sathish.ecommerce.multivendor.modal.User;
import com.sathish.ecommerce.multivendor.repository.UserRepository;
import com.sathish.ecommerce.multivendor.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) {
        // From this jwt token we will get email id this email id find the user.

        String email = jwtProvider.getEmailFromJwtToken(jwt);
        User user = this.findUserByEmail(email);
        if(user == null){
            throw new Exception("user not found with email - "+ email)
        }
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }
}
