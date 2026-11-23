package com.sathish.ecommerce.multivendor.response;

import lombok.Data;

//@Data annotation so that getter & setter methods are available
@Data
public class LoginRequest {
    private String email;
    private String otp;
}
