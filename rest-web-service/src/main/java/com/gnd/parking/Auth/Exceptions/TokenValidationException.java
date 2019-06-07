package com.gnd.parking.Auth.Exceptions;

public class TokenValidationException extends Exception {
    public TokenValidationException(String message) {
        super(message);
    }
}
