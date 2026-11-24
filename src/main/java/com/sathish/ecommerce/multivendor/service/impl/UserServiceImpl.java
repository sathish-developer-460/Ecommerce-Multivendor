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
    public User findUserByJwtToken(String jwt) throws Exception {
        // From this jwt token we will get email id this email id find the user.

        String email = jwtProvider.getEmailFromJwtToken(jwt);
        User user = this.findUserByEmail(email);
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("user not found with email - " + email);
        }
        return user;
    }
}
