package com.gnd.parking.Auth.Exceptions;

public class TokenSignatureVerificationException extends TokenValidationException {
    public TokenSignatureVerificationException() {
        super("Wrong token signature.");
    }
}
