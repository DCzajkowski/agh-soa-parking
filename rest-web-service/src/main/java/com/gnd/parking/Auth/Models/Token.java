package com.gnd.parking.Auth.Models;

import com.gnd.parking.Auth.Exceptions.TokenParseException;
import com.gnd.parking.Models.Role;
import com.nimbusds.jwt.SignedJWT;

import java.security.Principal;
import java.text.ParseException;

public class Token implements Principal {
    private SignedJWT token;

    public Token(SignedJWT token) {
        this.token = token;
    }

    public Role getRole() {
        try {
            return Role.valueOf((String) token.getJWTClaimsSet().getClaim("role"));
        } catch (ParseException e) {
            throw new TokenParseException();
        }
    }

    public int getRegionId() {
        try {
            return (int) token.getJWTClaimsSet().getClaim("region_id");
        } catch (ParseException e) {
            throw new TokenParseException();
        }
    }

    @Override
    public String getName() {
        try {
            return token.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new TokenParseException();
        }
    }
}
