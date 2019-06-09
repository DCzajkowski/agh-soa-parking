package com.gnd.parking.Responses;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.gnd.parking.Auth.Models.Token;

public class MeResponse {
    private Token user;

    public MeResponse(Token token) {
        user = token;
    }

    @JsonGetter("user")
    public Token getUser() {
        return user;
    }
}
