package com.gnd.parking.Auth.Models;

import com.gnd.parking.Auth.Exceptions.TokenParseException;
import com.gnd.parking.Models.Role;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public class Token {
    private SignedJWT token;

    public Token(SignedJWT token) {
        this.token = token;
    }

    public String getUsername() throws TokenParseException {
        try {
            return token.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new TokenParseException();
        }
    }

    public Role getRole() throws TokenParseException {
        try {
            return Role.valueOf((String) token.getJWTClaimsSet().getClaim("role"));
        } catch (ParseException e) {
            throw new TokenParseException();
        }
    }
}
