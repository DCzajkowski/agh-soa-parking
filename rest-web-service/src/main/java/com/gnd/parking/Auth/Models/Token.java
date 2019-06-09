package com.gnd.parking.Auth.Models;

import com.fasterxml.jackson.annotation.JsonGetter;
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

    private Integer getInteger(String claim) {
        try {
            Long value = (Long) token.getJWTClaimsSet().getClaim(claim);

            return value != null ? value.intValue() : null;
        } catch (ParseException e) {
            throw new TokenParseException();
        }
    }

    @JsonGetter("id")
    public Integer getId() {
        return getInteger("id");
    }

    @JsonGetter("role")
    public Role getRole() {
        try {
            return Role.valueOf((String) token.getJWTClaimsSet().getClaim("role"));
        } catch (ParseException e) {
            throw new TokenParseException();
        }
    }

    @JsonGetter("region_id")
    public Integer getRegionId() {
        return getInteger("region_id");
    }

    @Override
    @JsonGetter("username")
    public String getName() {
        try {
            return token.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new TokenParseException();
        }
    }
}
