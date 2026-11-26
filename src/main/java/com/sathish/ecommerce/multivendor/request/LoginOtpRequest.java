package com.sathish.ecommerce.multivendor.request;

import com.sathish.ecommerce.multivendor.domain.USER_ROLE;
import lombok.Data;

@Data
public class LoginOtpRequest {
    private String email;
    private String otp;
    private USER_ROLE role;
}
