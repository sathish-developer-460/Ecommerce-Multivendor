package com.sathish.ecommerce.multivendor.exceptions;

// Unchecked, so callers don't need "throws SellerException" everywhere
public class SellerException extends RuntimeException {
    public SellerException(String message) {
        super(message);
    }
}