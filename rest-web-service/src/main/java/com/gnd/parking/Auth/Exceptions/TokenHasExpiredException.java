package com.gnd.parking.Auth.Exceptions;

public class TokenHasExpiredException extends TokenValidationException {
    public TokenHasExpiredException() {
        super("Token has expired.");
    }
}
