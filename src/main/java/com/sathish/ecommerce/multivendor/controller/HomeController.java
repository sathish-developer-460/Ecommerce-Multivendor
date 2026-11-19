package com.sathish.ecommerce.multivendor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sathish.ecommerce.multivendor.response.ApiResponse;

@RestController
public class HomeController {
    // @GetMapping
    // public String HomeControllerHandler(){
    // return "Welcome to Ecommerce Multivendor Application";
    // }
    @GetMapping("/")
    public ApiResponse HomeControllerHandler() {
        ApiResponse response = new ApiResponse();
        response.setMessage("Welcome to Ecommerce Multivendor Application");
        return response;
    }
}
