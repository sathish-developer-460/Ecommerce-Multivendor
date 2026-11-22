package com.sathish.ecommerce.multivendor.response;

import com.sathish.ecommerce.multivendor.domain.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private USER_ROLE role;
}
