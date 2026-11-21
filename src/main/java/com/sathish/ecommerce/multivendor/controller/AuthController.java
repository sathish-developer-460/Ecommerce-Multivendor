package com.sathish.ecommerce.multivendor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sathish.ecommerce.multivendor.response.SignupRequest;

import lombok.RequiredArgsConstructor;

import com.sathish.ecommerce.multivendor.modal.User;
import com.sathish.ecommerce.multivendor.repository.UserRepository;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<User> createUserHandler(@RequestBody SignupRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        // Insert user into User table (database)
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
}
