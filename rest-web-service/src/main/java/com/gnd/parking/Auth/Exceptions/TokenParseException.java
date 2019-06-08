package com.gnd.parking.Auth.Exceptions;

public class TokenParseException extends RuntimeException {
    public TokenParseException() {
        super("There was a problem with parsing of the token.");
    }
}
